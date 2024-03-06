package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.LanguageCode
import com.aes.common.Enums.MessageType
import com.aes.common.Models.MessageDTO
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Mappers.toStandardLanguage
import com.aes.smsservices.Models.RecievedMessageDTO
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RecieveSmsService(
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val translateSmsService: TranslateSmsService,
    val localQueueService: LocalQueueService
) : Logging {

    fun tagIncomingMessage(sms: Message) {
        //TODO change so only tags extracted agricultural question
        if (sms.messageTopics.isEmpty()) {
            localQueueService.writeItemToQueue("message_tag_queue", sms)
        }
    }

    fun sendToMessageHandler(sms: MessageDTO) {
        localQueueService.writeItemToQueue("message_handler_queue", sms)
    }

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
                    id = UUID.randomUUID(),
                    phoneNumber = listOf(resource.phoneNumber),
                    preferredLanguage = LanguageCode.fromLanguage(getLanguageCodeForCountry(resource.country).toStandardLanguage()) ?: LanguageCode.EN,
                    stopCollectingInformation = false
                )
            )
        }

        val lang = user.preferredLanguage ?: LanguageCode.EN

        if (lang != LanguageCode.EN) {
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