package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.KnowledgeAreaId
import com.aes.common.Entities.Message
import com.aes.common.Entities.MessageTopics
import com.aes.common.Models.TaggingMessage
import com.aes.common.Repositories.KnowledgeAreaRepository
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.logging.Logging
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageTaggingService(
    val knowledgeEvaluator: KnowledgeEvaluator,
    val knowledgeRepository: KnowledgeAreaRepository,
    val messageTopicsRepository: MessageTopicsRepository,
    val messageRepository: MessageRepository
) : Logging {
    @Transactional
    fun tagMessage(messageToTag: TaggingMessage) {
        val crop = knowledgeEvaluator.getCropOfMessage(messageToTag.message)
        val topic = knowledgeEvaluator.getTopicOfMessage(messageToTag.message)


        if (crop != null && topic != null) {
            val area = knowledgeRepository.findByIdOrNull(KnowledgeAreaId(topic, crop))
                ?: knowledgeRepository.save(KnowledgeArea(topic, crop, LocalDateTime.now()))
            val message = messageRepository.findMessageByUser_IdAndTypeAndCreatedAtMin(messageToTag.userId)!!
            val topics = messageTopicsRepository.save(MessageTopics(area, message))
            if (!message.messageTopics.any { it.sms.id == message.id && it.knowledgeArea.topic == topic && it.knowledgeArea.cropName == crop }) {
                message.messageTopics.add(topics)
            }

        }
    }
}