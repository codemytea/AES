package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.KnowledgeAreaId
import com.aes.common.Entities.Message
import com.aes.common.Entities.MessageTopics
import com.aes.common.Repositories.KnowledgeAreaRepository
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MessageTaggingService(
    val knowledgeEvaluator: KnowledgeEvaluator,
    val knowledgeRepository: KnowledgeAreaRepository,
    val messageTopicsRepository: MessageTopicsRepository,
    val messageRepository: MessageRepository
): Logging {
    @Transactional
    fun tagMessage(sms: Message){
        val crop = knowledgeEvaluator.getCropOfMessage(sms.message)
        val topic = knowledgeEvaluator.getTopicOfMessage(sms.message)

        logger().info("Message crop is $crop and topic is $topic")

        if (crop != null && topic != null) {
            val area = knowledgeRepository.findByIdOrNull(KnowledgeAreaId(topic, crop))
                ?: knowledgeRepository.save(KnowledgeArea(topic, crop))
            val message = messageRepository.findByIdOrNull(sms.id)!!
            val topics = messageTopicsRepository.save(MessageTopics(area, message))
            if(!message.messageTopics.any { it.sms.id == message.id && it.knowledgeArea.topic == topic && it.knowledgeArea.cropName == crop }) {
                message.messageTopics.add(topics)
            }

        }
    }
}