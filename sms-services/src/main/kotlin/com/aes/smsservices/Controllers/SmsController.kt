package com.aes.smsservices.Controllers

import com.aes.common.Enums.MessageStatus
import com.aes.common.Models.MessageDTO
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Mappers.toDTO
import com.aes.smsservices.Mappers.toNewMessageDTO
import com.aes.smsservices.Models.MessageStatusDTO
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.smsservices.Services.RecieveSmsService
import com.aes.smsservices.Services.SendSmsService
import com.aes.smsservices.Services.UpdateSmsService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/message")
class SmsController(
    val recieveSmsService: RecieveSmsService,
    val updateSmsService: UpdateSmsService,
    val sendSmsService: SendSmsService,
    val messageRepository: MessageRepository,
) : Logging {

    /**
     * Webhook for receiving incoming messages from users
     *
     * @param resource - the recieved message
     * @return the saved message dto
     * */
    @PostMapping("/receive")
    fun receiveSMS(@RequestBody resource: RecievedMessageDTO): MessageDTO {
        logger().info("Received Message with id ${resource.id} and message ${resource.message}.")

        //Translate the message into English if necessary and save to the DB to the associated user.
        //If no associated user, create a new one and determine preferred language
        val sms = recieveSmsService.save(resource)

        //
        logger().info("Sending ${sms.id} to message handler.")
        recieveSmsService.sendToMessageHandler(sms)

        return sms.toDTO()
    }

    /**
     * Receives SMS status changes
     *
     * @param resource - the incoming message status dto
     * @return the resulting message dto
     * */
    @PostMapping("/status")
    fun smsStatusChange(@RequestBody resource: MessageStatusDTO): MessageDTO {
        logger().info(
            "Received Message Status update for message with id " +
                    "${resource.id} and new status ${resource.status.name}, ${resource.code}, ${resource.error}"
        )


        return updateSmsService.update(resource).toDTO().also {
            if (resource.status == MessageStatus.FAILED || resource.status == MessageStatus.REJECTED){
                messageRepository.findByIdOrNull(resource.id)?.message?.let {
                    sendSmsService.sendSMS(
                        resource.toNewMessageDTO(it)
                    )
                }

            }
        }
    }
}




