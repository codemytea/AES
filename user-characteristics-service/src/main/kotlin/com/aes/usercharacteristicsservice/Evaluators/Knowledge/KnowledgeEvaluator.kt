package com.aes.usercharacteristicsservice.Evaluators.Knowledge

import com.aes.common.Entities.User
import com.aes.common.Entities.UserKnowledge
import com.aes.common.Enums.Age
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Repositories.MessageRepository
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.exp

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

    /**
     * Calculates a user's expertise on a given Knowledge Area (KA) given all their messages.
     * This basic model assumes that the more questions asked about a KA, the more the user doesn't know.
     * This gets more accurate over more and more crop cycles.
     *
     * Ebbinghaus curve of forgetting is also included as the longer it's passed since the user has asked a question
     * about a particular topic, the more they will have forgotten about it.
     * */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    //@Scheduled(cron = "0/10 * * ? * *")
    fun calculateUserExpertiseOfTopicAndCrop() {

        userRepository.findAll().forEach { user ->
            val messages = messageRepository.getMessageByUserIdAndType(user.id)?.map {
                it.messageTopics.firstOrNull()
            }

            messages?.groupBy {
                it?.knowledgeArea
            }?.mapNotNull {
                (it.key ?: return@mapNotNull null) to it.value.size
            }?.sortedByDescending {
                it.second
            }?.onEach {

                val lastInteractionTime = user.lastInteractionTime[it.first] ?: it.first.modifiedAt

                val decayFactor = calculateDecayFactor(user, lastInteractionTime, LocalDateTime.now())
                val scaledKnowledge = Utils.scaleProbability(
                    it.second.toDouble(),
                    messages.size.toDouble(),
                    true
                )
                logger().info("User ${user.id} estimated knowledge on crop ${it.first} is $scaledKnowledge")
                if (!user.knowledgeAreas.any { ka ->
                        ka.topic == it.first.topic && ka.crop == it.first.cropName
                    }) {
                    val ka = userKnowledgeRepository.save(
                        UserKnowledge(
                            user.id,
                            //btw 0.0 and 1.0 where 0.0 = most messages and tfr least knowledgeable
                            scaledKnowledge * decayFactor,
                            it.first.topic,
                            it.first.cropName
                        )
                    )
                    user.knowledgeAreas.add(ka)
                }
            }
        }
    }

    /**
     * Uses Ebbinghaus' curve of forgetting to add a decay factor to user expertise calculations.
     *
     * @param user - the user this is being calculated for
     * @param lastInteractionTime - the last time the user has asked a question about this topic
     * @param currentTime - the time the calculation is being performed
     *
     * @return the decay factor
     * */
    fun calculateDecayFactor(user: User, lastInteractionTime: LocalDateTime, currentTime: LocalDateTime): Double {
        val daysSinceLastInteraction = ChronoUnit.DAYS.between(lastInteractionTime, currentTime)
        val halfLife = calculateHalfLife(user.age ?: Age.ADULT, user.literacy ?: 50.0f)

        // Apply the Ebbinghaus forgetting curve formula
        return exp(-0.693 * daysSinceLastInteraction / halfLife)
    }


    /**
     * Different users will have different half lives. This provides a rudimentary example calculation based on a
     * users age and literacy level.
     *
     * @param age - the users age
     * @param literacy - the users literacy level
     * */
    fun calculateHalfLife(age: Age, literacy: Float): Double {
        val baseHalfLife = 1.0 // Base half-life

        val ageHalfLife = when (age) {
            Age.YOUNG -> baseHalfLife * 0.8
            Age.ADULT -> baseHalfLife
            Age.AGED -> baseHalfLife * 0.8
        }

        val literacyAdjustment = 1.0 - (literacy / 100.0) // Convert literacy to a factor between 0 and 1
        val literacyHalfLife = baseHalfLife * (1.0 + literacyAdjustment * 0.2)

        return (ageHalfLife + literacyHalfLife) / 2.0
    }
}
