package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.ReferAFriendExtraction
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(4)
@Service
class ReferAFriendDetector(
    private val referAFriendExtraction: ReferAFriendExtraction
) : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.REFER_FRIEND

    override fun extractPartAndReturnRemaining(remainingMessage: String, userID : UUID): List<String>? {
        return referAFriendExtraction.getReferral(remainingMessage).mapNotNull { it }.ifEmpty { null }
    }

    override fun generateAnswer(prompts : List<String>, userID : UUID): List<String>? {
        //TODO("Not yet implemented")
        //ner
        //save to DB
        //send friend a message
        return listOf("Thank you for your referral.")
    }
}