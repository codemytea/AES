package com.aes.servicesendsms.Controller
import com.aes.servicesendsms.Model.ResponseDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.servicesendsms.Service.SMSSendService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.serviceshared.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@RestController("/rest")
class SendSMSController(
    val smsSendService: SMSSendService,
    val userRepository : UserRepository,
) : Logging {

    @PostMapping("/mtsms")
    fun sendSMS(@RequestBody resource: SendMessageDTO) : ResponseDTO? {
        logger().info("Sending message with ID ${resource.userref}")

        //userRepository.findById("").get().message_history.add(resource)
        return smsSendService.sendMessage(resource)
    }

}
//class xyz
//
//@Component
//class util{
//    @Bean
//    fun getxyz(): xyz{
//        return xyz()
//    }
//}
