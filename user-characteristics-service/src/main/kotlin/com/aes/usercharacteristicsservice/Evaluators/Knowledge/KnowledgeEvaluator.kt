package com.aes.usercharacteristicsservice.Evaluators.Knowledge

import com.aes.common.Entities.UserKnowledge
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.Repositories.UserKnowledgeRepository
import com.aes.common.Repositories.UserRepository
import com.aes.usercharacteristicsservice.Python.KnowledgeClassifiers
import com.aes.usercharacteristicsservice.Utilities.Utils
import org.springframework.stereotype.Service
import java.util.*

@Service
class KnowledgeEvaluator(
    private val knowledgeClassifier: KnowledgeClassifiers,
    private val messageRepository: MessageRepository,
    private val messageTopicsRepository: MessageTopicsRepository,
    private val userKnowledgeRepository: UserKnowledgeRepository,
    private val userRepository: UserRepository,
) {
    fun getTopicOfMessage(message: String?): Topic? {
        return if (message.isNullOrBlank()) null else knowledgeClassifier.getTopicOfMessage(listOf(message))
    }

    fun getCropOfMessage(message: String?): Crop? {
        return if (message.isNullOrBlank()) null else knowledgeClassifier.getCropOfMessage(listOf(message))
    }


    fun calculateUserExpertiseOfCrop(crop: Crop, userId: UUID): List<Pair<Crop, Int>> {
        val messages = messageRepository.getMessagesByUserId(userId).map {
            it.messageTopics.firstOrNull()
        }

        val crops = messages.groupBy {
            it?.knowledgeArea?.cropName
        }.mapNotNull {
            (it.key ?: return@mapNotNull null) to it.value.size
        }.sortedByDescending {
            it.second
        }.onEach {
            userKnowledgeRepository.save(
                UserKnowledge(
                    userRepository.findById(userId).get(),
                    //btw 0.0 and 1.0 where 0.0 = most messages and tfr least knowledgeable
                    Utils.scaleProbability(it.second.toDouble(), messages.size.toDouble(), true),
                    null,
                    it.first
                )
            )
        }

        return crops
    }

    fun calculateUserExpertiseOfTopic(userId: UUID): List<Pair<Topic, Int>> {
        val messages = messageRepository.getMessagesByUserId(userId).map {
            it.messageTopics.firstOrNull()
        }

        val topics = messages.groupBy {
            it?.knowledgeArea?.topic
        }.mapNotNull {
            (it.key ?: return@mapNotNull null) to it.value.size
        }.sortedByDescending {
            it.second
        }.onEach {
            userKnowledgeRepository.save(
                UserKnowledge(
                    userRepository.findById(userId).get(),
                    //btw 0.0 and 1.0 where 0.0 = most messages and tfr least knowledgeable
                    Utils.scaleProbability(it.second.toDouble(), messages.size.toDouble(), true),
                    it.first
                )
            )
        }

        return topics
    }
}
