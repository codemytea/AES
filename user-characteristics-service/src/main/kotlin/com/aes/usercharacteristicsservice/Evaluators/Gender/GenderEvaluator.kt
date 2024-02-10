package com.aes.usercharacteristicsservice.Evaluators.Gender;

import com.aes.common.Enums.Gender
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
class GenderEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val attributeEstimator: AttributeEstimator
) {

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    fun getGenderEstimate(userId: UUID): Gender? {
        val messages = messageRepository.getMessagesByUserId(userId)

        if (messages.isNullOrEmpty()) return null

        val gender = attributeEstimator.estimateGender(messages.map { it.message })

        val user = userRepository.findById(userId).get()
        user.gender = gender
        userRepository.save(user)

        return gender
    }
}
