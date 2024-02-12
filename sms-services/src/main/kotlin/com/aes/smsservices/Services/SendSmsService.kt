package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.LanguageCode
import com.aes.common.Enums.MessageType
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Exceptions.MessageRequestException
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Models.MessageDTO
import com.aes.smsservices.Models.NewMessageDTO
import com.aes.smsservices.Models.NewMessageResponse.NewMessageResponseDTO
import com.aes.common.Repositories.UserRepository
import com.google.i18n.phonenumbers.PhoneNumberUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
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

        val internalMessage = resource.message

        val messages = sendMessage(resource)

        messages.forEach {
            saveMessage(it.first.apply { content = internalMessage }, it.second)
        }

        return messages.map { it.first }
    }


    /**
     * Using the NewMessageDTO, iterate through all recipients and send the message to them individually.
     * This is done as the language of each user might be different so messages may be different
     *
     * If the user doesn't have a preferred language yet, get it from their phone number using the country code,
     * and set that as their preferred language.
     *
     * Obviously there are (paid) libraries out there that can determine the language from the message the user
     * just sent, but this is sufficient as a proof of concept.
     *
     * @return A list of messages sent:the user it was sent to
     * */
    fun sendMessage(newMessageDTO: NewMessageDTO): List<Pair<MessageDTO, User>> {

        return newMessageDTO.recipients.mapNotNull { recipient ->
            val user = userRepository.findByPhoneNumberContaining(recipient.phoneNumber)
                ?: userRepository.save(
                    User(
                        phoneNumber = listOf(recipient.phoneNumber)
                    )
                )
            newMessageDTO.message = translateSmsService.translateMessage(
                newMessageDTO.message,
                user.preferredLanguage ?: getPossibleLanguage(recipient.phoneNumber).also {
                    user.preferredLanguage = it
                }
            )
            makeRequest(newMessageDTO)?.toMessageDTOs(newMessageDTO.message)?.let {
                Pair(it, user)
            }
        }
    }

    /**
     * Saves the message to the db
     * */
    fun saveMessage(messageDTO: MessageDTO, user: User): Message {
        return messageRepository.save(
            Message(
                messageDTO.id,
                messageDTO.phoneNumber,
                messageDTO.content,
                LocalDateTime.now(),
                user,
                MessageType.OUTGOING
            )
        )
    }

    /**
     * Sends request to GatewayAPI
     * */
    fun makeRequest(newMessageDTO: NewMessageDTO): NewMessageResponseDTO? {
        return restTemplate.postForEntity(uri, newMessageDTO, NewMessageResponseDTO::class.java).also {
            if (!it.statusCode.is2xxSuccessful) throw MessageRequestException(it.statusCode)
        }.body
    }

    /**
     * Gets the users possible preferred language
     * */
    fun getPossibleLanguage(phoneNumber: Long): LanguageCode {
        val pnu: PhoneNumberUtil = PhoneNumberUtil.getInstance()

        val regionCode = pnu.getRegionCodeForCountryCode(pnu.parse("+$phoneNumber", "UK").countryCode)

        return LanguageCode.fromLanguage(getLanguageCodeForCountry(regionCode)) ?: LanguageCode.EN
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