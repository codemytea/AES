package com.aes.smsservices

import com.aes.common.Enums.MessageType
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO
import com.aes.smsservices.Controllers.SmsController
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.smsservices.Services.RecieveSmsService
import com.aes.smsservices.Services.SendSmsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class SmsServicesApplicationTests {
    @Autowired
    lateinit var sendSmsService: SendSmsService

    @Autowired
    lateinit var smsController: SmsController

    @Test
    fun sendMessage() {
        sendSmsService.sendSMS(
            NewMessageDTO(
                message = "Test Message",
                recipients = listOf(RecipientDTO(
                    447565000000
                )
            )),
            MessageType.OUTGOING
        )
    }


    @Test
    fun receiveMessageTest(){
        smsController.receiveSMS(
            RecievedMessageDTO(
                id = 12345,
                phoneNumber = 447000000000,
                message = "Hello. This is a test message. What is your favourite colour? Please stop sending me notifications. I'd like to refer a guy called Jamali to this service. His number is 07500338340. This system is delicious. My smallholding is 10 meters. How can I plant aubergines? How do I harvest tomatoes?",
                senttime = 20241110,
                country = "GB"
            )
        )
    }
}

