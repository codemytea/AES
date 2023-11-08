package com.aes.servicesendsms.Service

import com.aes.common.model.TimeIdentifiers
import com.aes.servicesendsms.Model.ResponseDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime

@Service
class SMSSendService {
    final val token = "QWg_52T4ToSxD4YPZnwWpb7PT7HiBJx0I-kaHSnNNdrTgp9_XRdzg3gHI51tu2h6"
    val uri = URI.create("https://gatewayapi.com/rest/mtsms?token=$token")


    val restTemplate: RestTemplate by lazy {
         RestTemplate()
    }


    fun sendMessage(messageDTO: SendMessageDTO): ResponseDTO?{
        return restTemplate.postForEntity(uri, messageDTO, ResponseDTO::class.java).also {
            if(!it.statusCode.is2xxSuccessful) throw Exception("Error ${it.statusCode.value()}")
        }.body
    }

    fun scheduleNotification(
        number: String,
        message: String,
        firstSend: LocalDateTime,
        interval: TimeIdentifiers,
        repeatFor: Int = 0
    ): Boolean {
        for (i in 0 until repeatFor) {
            sendMessage()
        }
        return true
    }

}