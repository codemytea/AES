package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Repositories.UserRepository
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.Stop
import com.aes.messagehandler.Python.StopExtraction
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(2)
@Service
class StopRequestDetector(
    private val stopExtraction: StopExtraction,
    private val userRepository: UserRepository,
) : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.STOP

    override fun extractPartAndReturn(
        remainingMessage: String,
        userID: UUID,
    ): List<String>? {
        val stopRequest = stopExtraction.getStopRequests(remainingMessage)
        if (stopRequest.isNullOrEmpty()) return listOf()

        val toReturn = mutableListOf<String>()

        stopRequest.forEach {
            toReturn.add(it[0])
            userRepository.findUserById(userID)?.let { user ->
                if (it[1] == Stop.INFORMATION.str) {
                    user.stopCollectingInformation = true
                } else {
                    user.stopSendingNotifications = true
                }
            }
        }

        return toReturn
    }

    override fun generateAnswer(
        prompts: List<String>,
        userID: UUID,
    ): List<String>? {
        return listOf("I will stop.")
    }
}
