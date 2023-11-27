package com.aes.servicesendsms.Service

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.common.model.TimeIdentifiers
import com.aes.servicesendsms.Model.NewMessageDTO
import com.aes.servicesendsms.Model.ResponseDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.servicesendsms.Utils.toMessageDTO
import com.aes.serviceshared.Models.LanguageCode
import com.aes.serviceshared.Models.MessageDTO
import com.aes.serviceshared.Models.MessageStatus
import com.aes.serviceshared.Models.MessageType
import com.aes.serviceshared.entities.KnowledgeArea
import com.aes.serviceshared.entities.Message
import com.aes.serviceshared.entities.MessageTopics
import com.aes.serviceshared.repositories.MessageRepository
import com.aes.serviceshared.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Service
class SMSSendService(
    val userRepository: UserRepository,
    val messageRepository: MessageRepository
) : Logging {
    final val token = "QWg_52T4ToSxD4YPZnwWpb7PT7HiBJx0I-kaHSnNNdrTgp9_XRdzg3gHI51tu2h6"
    val uri = URI.create("https://gatewayapi.com/rest/mtsms?token=$token")

    val restTemplate: RestTemplate by lazy {
         RestTemplate()
    }

    @Transactional
    fun create(message: NewMessageDTO): ResponseDTO? {
        logger().info("Creating a new sms resource.")
        val user = userRepository.findByIdOrNull(
            UUID.fromString(message.userId)) ?: throw Exception("Could not find user ${message.userId}")
        val m = Message(
            message.message,
            LocalDateTime.now(),
            MessageType.OUTGOING,
            MessageStatus.PENDING,
            LanguageCode.EN
        )
        return messageRepository.save(m).let{
            it.user = user
            sendMessage(it.toMessageDTO())
        }
    }

    fun sendMessage(messageDTO: SendMessageDTO): ResponseDTO?{
        return restTemplate.postForEntity(uri, messageDTO, ResponseDTO::class.java).also {
            if(!it.statusCode.is2xxSuccessful) throw Exception("Error ${it.statusCode.value()}")
        }.body
    }
}











/*
    fun scheduleNotification(
        number: String,
        message: String,
        firstSend: LocalDateTime,
        interval: TimeIdentifiers,
        repeatFor: Int = 0
    ): Boolean {
        for (i in 0 until repeatFor) {
            //sendMessage()
        }
        return true
    }
 */