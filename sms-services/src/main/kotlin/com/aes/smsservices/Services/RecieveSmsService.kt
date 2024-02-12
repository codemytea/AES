package com.aes.smsservices.Services

import com.aes.common.Entities.*
import com.aes.common.Enums.LanguageCode
import com.aes.common.Enums.MessageType
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Models.RecievedMessageDTO
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.Repositories.UserKnowledgeRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.logger
import com.aes.smsservices.Repositories.KnowledgeAreaRepository
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
    val knowledgeRepository: KnowledgeAreaRepository,
    val knowledgeEvaluator: KnowledgeEvaluator
) : Logging {

    @Transactional
    fun tagIncomingMessage(sms: Message) {
        if (sms.messageTopics.isEmpty()){
            val crop = knowledgeEvaluator.getCropOfMessage(sms.message)
            val topic = knowledgeEvaluator.getTopicOfMessage(sms.message)

            logger().info("Message crop is $crop and topic is $topic")

            if (crop != null && topic != null){
                knowledgeRepository.save(KnowledgeArea(topic, crop))
                messageTopicsRepository.save(MessageTopics(knowledgeRepository.findById(KnowledgeAreaId(topic, crop)).get(), sms))
            }
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