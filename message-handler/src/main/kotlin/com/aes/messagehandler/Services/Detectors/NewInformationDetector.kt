package com.aes.messagehandler.Services.Detectors

import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.InformationCollection
import com.aes.messagehandler.Services.NewInformationService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(4)
@Service
class NewInformationDetector(
    private val newInformationService: NewInformationService,
    private val userRepository: UserRepository,
    private val informationCollection: InformationCollection
) : MessageHandler, Logging {

    override val messagePartType: HandlableMessageType = HandlableMessageType.INFORMATION

    override fun extractPartAndReturn(remainingMessage: String, userID: UUID): List<String>? {
        newInformationService.getDetailsToDetermine(userID)?.let {
            newInformationService.saveNewInformation(remainingMessage, userID, it)
            return informationCollection.removeNewInformation(remainingMessage, it).also{
                logger().info("collected following new piece of information: ${it.joinToString("")}")
            }
        }
        return null
    }

    override fun generateAnswer(prompts: List<String>, userID: UUID): List<String>? {
        val userChoice = userRepository.findUserById(userID)?.stopCollectingInformation
        var callToAction: String? = null

        //get remaining details to determine
        newInformationService.getDetailsToDetermine(userID)?.let {
            if (userChoice != true) {
                logger().info("Asking user with id $userID for following new information: $it")
                callToAction = informationCollection.collect(it)
                logger().info("Asking user with id $userID for following new information: $it with call to action: $callToAction")
            } else {
                logger().info("User with id $userID has asked for stop in info collection")
            }
        }
        return callToAction?.let { listOf(it) }
    }

//TODO response included info that user had just given
}