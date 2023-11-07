package com.aes.servicesendsms.Controller
import com.aes.servicesendsms.Model.ResponseDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.servicesendsms.Service.SMSSendService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController("/rest")
class SendSMSController(
    val smsSendService: SMSSendService
) {

    @PostMapping("/mtsms")
    fun sendSMS(@RequestBody resource: SendMessageDTO) : ResponseDTO? {
        Logger.getAnonymousLogger().info("Sending message with ID ${resource.userref}")

        return smsSendService.sendMessage(resource)
    }
}
