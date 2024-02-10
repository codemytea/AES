package com.aes.usercharacteristicsservice.Evaluators.Age

import com.aes.common.Enums.Age
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
@Configuration
@EnableScheduling
class AgeEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val attributeEstimator: AttributeEstimator
) {

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    fun getAgeEstimate(userId: UUID): Age? {
        val messages = messageRepository.getMessagesByUserId(userId)

        if (messages.isNullOrEmpty()) return null

        val age = attributeEstimator.estimateAge(messages.map { it.message })

        val user = userRepository.findById(userId).get()
        user.age = age
        userRepository.save(user)

        return age
    }
}