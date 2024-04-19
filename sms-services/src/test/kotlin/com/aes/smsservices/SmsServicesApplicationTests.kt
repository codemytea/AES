package com.aes.smsservices

import com.aes.common.Enums.MessageType
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO
import com.aes.smsservices.Services.SendSmsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SmsServicesApplicationTests {
    @Autowired
    lateinit var sendSmsService: SendSmsService


    @Test
    fun sendMessage() {
        sendSmsService.sendSMS(
            NewMessageDTO(
                message = "this is a test!",
                recipient = RecipientDTO(
                    447565533834
                )
            ),
            MessageType.OUTGOING
        )
    }
}

