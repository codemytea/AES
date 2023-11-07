package com.aes.servicerecievesms.Controller

import com.aes.servicerecievesms.Model.RecievedSMSDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController("/message")
class RecieveSMSController() {

    @PostMapping("/receive")
    fun receiveSMS(@RequestBody resource: RecievedSMSDTO) {
        Logger.getAnonymousLogger().info("Received Message !! ${resource.message}")
    }


}