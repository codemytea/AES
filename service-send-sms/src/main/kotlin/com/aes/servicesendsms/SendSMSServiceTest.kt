package com.aes.servicesendsms

import com.aes.servicesendsms.Model.NewMessageDTO
import com.aes.servicesendsms.Model.RecipientDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.servicesendsms.Service.SMSSendService
import com.aes.serviceshared.Models.Crop
import com.aes.serviceshared.Models.Topic
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SendSMSServiceTest(
    val sendSMSService: SMSSendService
): ApplicationRunner{

    override fun run(args: ApplicationArguments?) {

        sendSMSService.create(NewMessageDTO(
            "efbbbf30-6231-0000-0000-000000000000",
            "Test message :)",
        ))
    }
}