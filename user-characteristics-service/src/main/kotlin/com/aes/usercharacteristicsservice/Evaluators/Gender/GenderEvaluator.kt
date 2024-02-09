package com.aes.usercharacteristicsservice.Evaluators.Gender;

import com.aes.smsservices.Repositories.MessageRepository
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import com.aes.usercharacteristicsservice.Enums.Gender
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenderEvaluator(
    private val messageRepository: MessageRepository,
    private val attributeEstimator: AttributeEstimator
) {
    fun getGenderEstimate(userId: UUID): Gender? {
        val messages = messageRepository.getMessagesByUser(userId)

        if (messages.isNullOrEmpty()) return null

        return attributeEstimator.estimateGender(messages.map { it.message })
    }
}
