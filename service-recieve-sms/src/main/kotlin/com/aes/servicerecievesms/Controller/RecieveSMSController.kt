package com.aes.servicerecievesms.Controller

import com.aes.servicerecievesms.Model.RecievedSMSDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.aes.common.logging.Logging
import com.aes.common.logging.logger

@RestController("/message")
class RecieveSMSController : Logging {

    @PostMapping("/receive")
    fun receiveSMS(@RequestBody resource: RecievedSMSDTO) {
        logger().info("Received Message !! ${resource.message}")
    }


}