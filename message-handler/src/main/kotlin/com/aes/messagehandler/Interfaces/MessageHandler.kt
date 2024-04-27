package com.aes.messagehandler.Interfaces

import com.aes.common.Enums.HandlableMessageType
import java.util.*

interface MessageHandler {
    val messagePartType: HandlableMessageType
    fun extractPartAndReturn(remainingMessage: String, userID: UUID): List<String>?
    fun generateAnswer(prompts : List<String>, userID: UUID): List<String>?
}



