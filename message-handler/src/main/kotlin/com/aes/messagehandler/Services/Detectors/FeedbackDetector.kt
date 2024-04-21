package com.aes.messagehandler.Services.Detectors

import com.aes.common.Entities.UserFeedback
import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Repositories.UserFeedbackRepository
import com.aes.common.Repositories.UserRepository
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.FeedbackExtraction
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(5)
@Service
class FeedbackDetector(
    private val feedbackExtraction: FeedbackExtraction,
    private val userFeedbackRepository: UserFeedbackRepository,
    private val userRepository: UserRepository,
) : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.FEEDBACK

    override fun extractPartAndReturnRemaining(remainingMessage: String, userID: UUID): List<String>? {
        //extract the agricultural questions using OpenAI
        return feedbackExtraction.getFeedback(remainingMessage).mapNotNull { it }.ifEmpty { null }
    }

    override fun generateAnswer(prompts : List<String>, userID: UUID): List<String>? {
        //TODO("Not yet implemented")
        userRepository.findUserById(userID)?.let { user ->
            prompts.forEach {
                userFeedbackRepository.save(
                    UserFeedback(
                        UUID.randomUUID(),
                        user,
                        it
                    )
                )
            }
        }

        return listOf("Thank you for your feedback.")
    }
}