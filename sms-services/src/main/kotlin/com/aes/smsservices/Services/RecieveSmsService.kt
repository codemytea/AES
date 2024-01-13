package com.aes.smsservices.Services

import com.aes.common.logging.Logging
import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Entities.User
import com.aes.smsservices.Enums.LanguageCode
import com.aes.smsservices.Enums.MessageType
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.smsservices.Repositories.MessageRepository
import com.aes.smsservices.Repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RecieveSmsService(
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val translateSmsService: TranslateSmsService
) : Logging {

    /**
     * Saves incoming message to db, in english
     *
     * @param resource the received message
     *
     * @return the message entity
     * */
    @Transactional
    fun save(resource: RecievedMessageDTO): Message {

        val user = userRepository.findByPhoneNumberContaining(resource.phoneNumber) ?: let {
            userRepository.save(
                User(
                    UUID.randomUUID(),
                    listOf(resource.phoneNumber),
                    LanguageCode.fromLanguage(getLanguageCodeForCountry(resource.country)) ?: LanguageCode.EN,
                )
            )
        }

        val lang = user.preferredLanguage ?: LanguageCode.EN

        if (lang != LanguageCode.EN){
            resource.message = translateSmsService.translateMessage(resource.message, fromLanguage = lang)
        }

        return Message(
            resource.id,
            resource.phoneNumber,
            resource.message,
            LocalDateTime.now(),
            user,
            MessageType.INCOMING
        ).also { messageRepository.save(it) }
    }


}