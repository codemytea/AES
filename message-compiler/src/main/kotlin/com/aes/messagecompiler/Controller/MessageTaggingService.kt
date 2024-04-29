package com.aes.messagecompiler.Controller

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.KnowledgeAreaId
import com.aes.common.Entities.MessageTopics
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Repositories.KnowledgeAreaRepository
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.logging.Logging
import com.aes.messagecompiler.Python.KnowledgeClassifiers
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MessageTaggingService(
    val knowledgeRepository: KnowledgeAreaRepository,
    val messageTopicsRepository: MessageTopicsRepository,
    val messageRepository: MessageRepository,
    val knowledgeClassifier: KnowledgeClassifiers,
) : Logging {
    fun getTopicOfMessage(message: String): Topic? {
        return knowledgeClassifier.getTopicOfMessage(listOf(message))
    }

    fun getCropOfMessage(message: String): Crop? {
        return knowledgeClassifier.getCropOfMessage(listOf(message))
    }

    @Transactional
    fun tagMessage(
        question: String,
        userId: UUID,
    ): KnowledgeArea? {
        val crop = getCropOfMessage(question)
        val topic = getTopicOfMessage(question)

        if (crop != null && topic != null) {
            return knowledgeRepository.findByIdOrNull(KnowledgeAreaId(topic, crop))
                ?: knowledgeRepository.save(KnowledgeArea(topic, crop, LocalDateTime.now())).also {
                    val message = messageRepository.findFirstByUserIdAndTypeOrderByCreatedAtDesc(userId)!!
                    val topics = messageTopicsRepository.save(MessageTopics(it, message))
                    if (!message.messageTopics.any {
                            it.sms.id == message.id &&
                                it.knowledgeArea.topic == topic &&
                                it.knowledgeArea.cropName == crop
                        }
                    ) {
                        message.messageTopics.add(topics)
                    }
                }
        }
        return null
    }
}
