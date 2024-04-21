package com.aes.messagehandler.Services.Detectors

import com.aes.common.logging.Logging
import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.GeneralChatbot
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Service
@Order(6)
class GeneralChatDetector(
    private val generalChatbot: GeneralChatbot,
) : MessageHandler, Logging {
    override val messagePartType: HandlableMessageType = HandlableMessageType.GENERAL


    override fun detectMessagePartType(remainingMessage: String, userID: UUID): List<String>? {
        return if (remainingMessage.isBlank()) null else listOf(remainingMessage)
    }

    override fun generateAnswer(prompts: List<String>, userID: UUID): List<String>? {
        return listOf(generalChatbot.generalChatbot(prompts.joinToString(""))).mapNotNull { it }.ifEmpty {
            null
        }
    }
}