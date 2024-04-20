package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.MessageHandler
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(2)
@Service
class StopRequestDetector : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.STOP

    override fun detectMessagePartType(remainingMessage: String, userID : UUID): List<String>? {
        //save relevant stop info to db - need to check if info stop or notification stop
        //TODO("Not yet implemented")
        return null
    }

    override fun generateAnswer(prompts : List<String>, userID : UUID): List<String>? {
        //TODO("Not yet implemented")
        return null
    }

}