package com.aes.servicesendsms

import com.aes.servicesendsms.Model.RecipientDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.servicesendsms.Service.SMSSendService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SendSMSServiceTest(
    val sendSMSServiceTest: SMSSendService
): ApplicationRunner{

    override fun run(args: ApplicationArguments?) {
        sendSMSServiceTest.sendMessage(
            SendMessageDTO(
                message = "Test Message",
                sender = "447418372559",
                recipients = listOf(RecipientDTO(
                    447565533834
                ))

            )
        )

    }
}