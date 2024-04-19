package com.aes.messagehandler.Services.Detectors

import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.MessageHandler
import com.aes.messagehandler.Python.InformationCollection
import com.aes.messagehandler.Services.NewInformationService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(3)
@Service
class NewInformationDetector(
    private val newInformationService: NewInformationService,
    private val userRepository: UserRepository,
    private val informationCollection: InformationCollection
) : MessageHandler, Logging {

    override val messagePartType: HandlableMessageType = HandlableMessageType.INFORMATION

    override fun detectMessagePartType(remainingMessage: String, userID: UUID): List<String>? {
        //use openai?
        TODO("Not yet implemented")
    }

    /**
     * Get any new information provided in user message, save to DB, and, if user hasn't asked to stop information
     * collection, compile message asking for any remaining information.
     *
     * @param message - the message without agricultural questions in it
     * @return Pair("call to action collection message asking for more information", "leftover original message")
     * */
    override fun generateAnswer(prompts: List<String>, userID: UUID): List<String>? {
        val detailsToDetermine = newInformationService.getDetailsToDetermine(userID)
        newInformationService.saveNewInformation(prompts, userID, detailsToDetermine)

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


}