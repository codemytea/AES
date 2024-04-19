package com.aes.messagehandler

import com.aes.common.Enums.HandlableMessageType
import java.util.*

interface MessageHandler {

    val messagePartType: HandlableMessageType

    fun detectMessagePartType(remainingMessage: String, userID: UUID): List<String>?

    fun generateAnswer(prompts : List<String>, userID: UUID): List<String>?
}