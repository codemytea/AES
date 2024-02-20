package com.aes.usercharacteristicsservice.Evaluators.Knowledge

import com.aes.common.Entities.UserKnowledge
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.Repositories.UserKnowledgeRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Python.KnowledgeClassifiers
import com.aes.usercharacteristicsservice.Utilities.Utils
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
@Configuration
@EnableScheduling
class KnowledgeEvaluator(
    private val knowledgeClassifier: KnowledgeClassifiers,
    private val messageRepository: MessageRepository,
    private val userKnowledgeRepository: UserKnowledgeRepository,
    private val userRepository: UserRepository,
) : Logging {
    fun getTopicOfMessage(message: String): Topic? {
        return knowledgeClassifier.getTopicOfMessage(listOf(message))
    }

    fun getCropOfMessage(message: String): Crop? {
        return knowledgeClassifier.getCropOfMessage(listOf(message))
    }


    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    //@Scheduled(cron = "0/10 * * ? * *")
    fun calculateUserExpertiseOfTopicAndCrop() {

        userRepository.findAll().forEach { user ->
            val messages = messageRepository.getMessageByUserIdAndType(user.id).map {
                it.messageTopics.firstOrNull()
            }

            messages.groupBy {
                it?.knowledgeArea
            }.mapNotNull {
                (it.key ?: return@mapNotNull null) to it.value.size
            }.sortedByDescending {
                it.second
            }.onEach {
                val scaledKnowledge = Utils.scaleProbability(it.second.toDouble(), messages.size.toDouble(), true)
                logger().info("User ${user.id} estimated knowledge on crop ${it.first} is $scaledKnowledge")
                if(!user.knowledgeAreas.any { ka->
                    ka.topic == it.first.topic && ka.crop == it.first.cropName
                }){
                    val ka = userKnowledgeRepository.save(
                        UserKnowledge(
                            user.id,
                            //btw 0.0 and 1.0 where 0.0 = most messages and tfr least knowledgeable
                            scaledKnowledge,
                            it.first.topic,
                            it.first.cropName
                        )
                    )
                    user.knowledgeAreas.add(ka)
                }

            }
        }

    }
}
