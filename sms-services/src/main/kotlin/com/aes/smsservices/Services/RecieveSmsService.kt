package com.aes.smsservices.Services

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.Message
import com.aes.common.Entities.MessageTopics
import com.aes.common.Entities.User
import com.aes.common.Enums.LanguageCode
import com.aes.common.Enums.MessageType
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.Repositories.UserRepository
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RecieveSmsService(
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val messageTopicsRepository: MessageTopicsRepository,
    val translateSmsService: TranslateSmsService,
    val knowledgeEvaluator: KnowledgeEvaluator
) : Logging {

    @Transactional
    fun tagIncomingMessage(smsId: Long) {
        val crop = knowledgeEvaluator.getCropOfMessage(messageRepository.getMessageById(smsId)?.message)
        val topic = knowledgeEvaluator.getTopicOfMessage(messageRepository.getMessageById(smsId)?.message)

        if (crop != null && topic != null){
            messageTopicsRepository.save(MessageTopics(KnowledgeArea(topic, crop)))
        }
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
                    UUID.randomUUID(),
                    listOf(resource.phoneNumber),
                    LanguageCode.fromLanguage(getLanguageCodeForCountry(resource.country)) ?: LanguageCode.EN,
                )
            )
        }

        val lang = user.preferredLanguage ?: LanguageCode.EN

        if (lang != LanguageCode.EN) {
            resource.message = translateSmsService.translateMessage(resource.message, fromLanguage = lang)
        }

        tagIncomingMessage(resource.id)

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