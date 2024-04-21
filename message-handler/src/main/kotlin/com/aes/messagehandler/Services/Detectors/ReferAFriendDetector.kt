package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.Interfaces.MessageHandler
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(4)
@Service
class ReferAFriendDetector : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.REFER_FRIEND

    override fun extractPartAndReturnRemaining(remainingMessage: String, userID : UUID): List<String>? {
        //TODO("Not yet implemented")
        return null
    }

    override fun generateAnswer(prompts : List<String>, userID : UUID): List<String>? {
        //TODO("Not yet implemented")
        return null
    }
}