package com.aes.servicerecievesms.Services

import com.aes.servicerecievesms.Model.Page
import com.aes.servicerecievesms.Model.RecievedSMSDTO
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import com.aes.common.logging.Logging
import com.aes.common.logging.logger

@Service
class RetrieveSMSService() : Logging {
    final val token = "QWg_52T4ToSxD4YPZnwWpb7PT7HiBJx0I-kaHSnNNdrTgp9_XRdzg3gHI51tu2h6"
    val uri = URI.create("https://gatewayapi.com/rest/mosms?token=$token&from=2023-09-09&to=2023-12-31")


    val restTemplate: RestTemplate by lazy {
        RestTemplate()
    }
    @Scheduled(fixedDelay = 10000)
    fun afterRun(){
        retrieveMessages()?.messages?.forEach{
            logger().info("MSG ${it.message}")
        }
    }


    fun retrieveMessages(): Page?{
        return restTemplate.getForEntity(uri, Page::class.java).also {
            if(!it.statusCode.is2xxSuccessful) throw Exception("Error ${it.statusCode.value()}")
        }.body
    }

}