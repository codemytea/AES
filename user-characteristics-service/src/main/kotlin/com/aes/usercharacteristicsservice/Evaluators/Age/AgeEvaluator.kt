package com.aes.usercharacteristicsservice.Evaluators.Age

import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Configuration
@EnableScheduling
class AgeEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val attributeEstimator: AttributeEstimator
) : Logging {

    /**
     * Estimates the age of the user given a list of all
     * their messages, using an Age Model from HuggingFace.
     * This estimated age is then saved to the DB
     * */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    fun getAgeEstimate() {
        userRepository.findAll().forEach { user ->
            val messages = messageRepository.getMessageByUserIdAndType(user.id).also {
                if (it.isNullOrEmpty()) return@forEach
            }
            val age = messages?.let { attributeEstimator.estimateAge(it.map { it.message }) }

            user.age = age
            userRepository.save(user)
            logger().info("User ${user.id} estimated age is $age")
        }
    }
}

