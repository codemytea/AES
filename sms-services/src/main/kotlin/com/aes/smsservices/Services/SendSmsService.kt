package com.aes.smsservices.Services

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Entities.User
import com.aes.smsservices.Enums.MessageType
import com.aes.smsservices.Exceptions.MessageRequestException
import com.aes.smsservices.Models.NewMessageDTO
import com.aes.smsservices.Models.MessageDTO
import com.aes.smsservices.Models.NewMessageResponse.NewMessageDetailsDTO
import com.aes.smsservices.Models.NewMessageResponse.NewMessageResponseDTO
import com.aes.smsservices.Repositories.MessageRepository
import com.aes.smsservices.Repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Service
class SendSmsService(
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val translateSmsService: TranslateSmsService
    //add in repositories here to save message to entities and find users etc
) : Logging {
    final val token = "QWg_52T4ToSxD4YPZnwWpb7PT7HiBJx0I-kaHSnNNdrTgp9_XRdzg3gHI51tu2h6"
    val uri = URI.create("https://gatewayapi.com/rest/mtsms?token=$token")

    val restTemplate: RestTemplate by lazy {
         RestTemplate()
    }

    /**
     * Method to send SMS to specified user
     * */

    @Transactional
    fun sendSMS(resource: NewMessageDTO): List<MessageDTO> {
        logger().info("Sending message at ${resource.sendtime}")

        val messages = sendMessage(resource)


        messages.forEach {
            saveMessage(it)
        }

        return messages
    }


    /*
* Other steps:
*
* - translate message into expected country content from phone number cc if no prev convo
* -> omg a referral system where other farmers give me their friends contact info if they like the system!
* - or try fetch user from base with msisnd, and if exists, get users target language
*
*
* => call translation service and then map that to field for content
* */
    fun sendMessage(newMessageDTO: NewMessageDTO): List<MessageDTO> {

        val temp = mutableListOf<MessageDTO>()

        newMessageDTO.recipients.forEach {
            val user = userRepository.findByPhoneNumberContaining(it.phoneNumber) ?: throw Exception("User with id ${it.phoneNumber} not found!")
            newMessageDTO.message = translateSmsService.translateMessage(newMessageDTO.message, /*toLanguage=user.language*/)
            temp.add(makeRequest(newMessageDTO)?.toMessageDTOs(newMessageDTO.message))
        }

        return temp
    }

    fun saveMessage(messageDTO: MessageDTO): Message{
        return messageRepository.save(
            Message(
                messageDTO.id,
                messageDTO.phoneNumber,
                messageDTO.content,
                LocalDateTime.now(),
                MessageType.OUTGOING,
                messageDTO.status,
                messageDTO.originalLanguage,
            )
        )
    }

    fun makeRequest(newMessageDTO: NewMessageDTO): NewMessageResponseDTO?{
        return restTemplate.postForEntity(uri, newMessageDTO, NewMessageResponseDTO::class.java).also {
            if(!it.statusCode.is2xxSuccessful) throw MessageRequestException(it.statusCode)
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