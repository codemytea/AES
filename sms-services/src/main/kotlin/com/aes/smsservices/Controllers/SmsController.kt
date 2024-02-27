package com.aes.smsservices.Controllers

import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.qachatbotservice.Information.InformationCollector
import com.aes.qachatbotservice.Python.InformationCollectionNER
import com.aes.smsservices.Mappers.toDTO
import com.aes.common.Models.MessageDTO
import com.aes.smsservices.Models.MessageStatusDTO
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.smsservices.Services.RecieveSmsService
import com.aes.smsservices.Services.SendSmsService
import com.aes.smsservices.Services.UpdateSmsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/message")
class SmsController(
    val recieveSmsService: RecieveSmsService,
    val updateSmsService: UpdateSmsService,
    val sendSmsService: SendSmsService,
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val informationCollectionNER: InformationCollectionNER,
    val informationCollector: InformationCollector,
) : Logging {

    /**
     * Webhook for receiving incoming messages from users
     * */
    @PostMapping("/receive")
    fun receiveSMS(@RequestBody resource: RecievedMessageDTO): MessageDTO {
        logger().info("Received Message with id ${resource.id} and message ${resource.message}")

        val sms = recieveSmsService.save(resource)

        logger().info("tagging message with id ${sms.id}")
        recieveSmsService.tagIncomingMessage(sms)

        //TODO if important flag, determine if message is giving detail or not, if not provide answer, if yes,
        userRepository.findByPhoneNumberContaining(resource.phoneNumber).also {
            if (it != null){
                if (messageRepository.isLatestMessageIncomingCollection(it)){
                    informationCollectionNER.collect()
                }
            }
        }

        //provide answer

        //TODO If more details to determine
        informationCollector.moreDetailsToDetermine(sms.message, sms.user.id).also {
            sendSmsService.collect(it, sms.user.phoneNumber.first()) //TODO set up flag somewhere notifying next response is important
        }

        return sms.toDTO()
    }

    /**
     * Changes status of sms eg from pending to delivered
     * */
    @PostMapping("/status")
    fun smsStatusChange(@RequestBody resource: MessageStatusDTO): MessageDTO {
        logger().info("Received Message Status update for message with id " +
                "${resource.id} and new status ${resource.status.name}")
        return updateSmsService.update(resource).toDTO()
    }
}




