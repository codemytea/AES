package com.aes.usercharacteristicsservice.Evaluators.Gender;

import com.aes.common.Enums.Gender
import com.aes.common.Repositories.MessageRepository
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenderEvaluator(
    private val messageRepository: MessageRepository,
    private val attributeEstimator: AttributeEstimator
) {
    fun getGenderEstimate(userId: UUID): Gender? {
        val messages = messageRepository.getMessagesByUserId(userId)

        if (messages.isNullOrEmpty()) return null

        return attributeEstimator.estimateGender(messages.map { it.message })
    }
}
