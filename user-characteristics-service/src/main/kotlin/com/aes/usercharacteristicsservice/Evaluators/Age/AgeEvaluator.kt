package com.aes.usercharacteristicsservice.Evaluators.Age

import com.aes.common.Enums.Age
import com.aes.common.Repositories.MessageRepository
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import java.util.*

class AgeEvaluator(
    private val messageRepository: MessageRepository,
    private val attributeEstimator: AttributeEstimator
) {
    fun getAgeEstimate(userId: UUID): Age? {
        val messages = messageRepository.getMessagesByUserId(userId)

        if (messages.isNullOrEmpty()) return null

        return attributeEstimator.estimateAge(messages.map { it.message })
    }
}