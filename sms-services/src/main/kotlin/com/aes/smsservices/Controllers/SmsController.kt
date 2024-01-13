package com.aes.smsservices.Controllers

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Mappers.toDTO
import com.aes.smsservices.Models.MessageDTO
import com.aes.smsservices.Models.MessageStatusDTO
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.smsservices.Services.RecieveSmsService
import com.aes.smsservices.Services.UpdateSmsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/message")
class SmsController(
    val recieveSmsService: RecieveSmsService,
    val updateSmsService: UpdateSmsService
) : Logging {

    /**
     * Webhook for receiving incoming messages from users
     * */
    @PostMapping("/receive")
    fun receiveSMS(@RequestBody resource: RecievedMessageDTO): MessageDTO {
        logger().info("Received Message with id ${resource.id} and message ${resource.message}")

        val sms = recieveSmsService.save(resource)

        return sms.toDTO()

    }

    /**
     * Changes status of sms eg from pending to delivered
     * */
    @PostMapping("/status")
    fun smsStatusChange(@RequestBody resource: MessageStatusDTO): MessageDTO {
        logger().info("Received Message Status update for message with id ${resource.id} and new status ${resource.status.name}")

        val sms = updateSmsService.update(resource)

        return sms.toDTO()

    }
}


