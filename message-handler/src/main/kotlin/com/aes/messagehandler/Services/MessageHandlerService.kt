package com.aes.messagehandler.Services

import com.aes.common.Entities.Message
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagecompiler.Controller.CompilerPipeline
import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Models.MessageQueueItem
import com.aes.common.Repositories.MessageRepository
import com.aes.messagehandler.MessageHandler
import com.aes.messagehandler.Utilities.ifNotNullOrEmpty
import com.aes.messagehandler.Utilities.replaceList
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessageHandlerService(
    private val messageHandler: List<MessageHandler>,
    private val compilerPipeline: CompilerPipeline,
    private val messageRepository: MessageRepository
) : Logging {


    /**
     * Sends a List of messages that need to be post-processed (tailored to user characteristics + limited to 255 chars)
     * ond what type there are. Leverages spring boot @Order to make it easily extensible.
     *
     * @param request - the incoming message
     * */
    @Transactional
    fun handleRequest(message: MessageQueueItem) {
        val request = messageRepository.findByIdOrNull(message.messageId)!!
        logger().info("Handling message handler request for message with id ${request.id}")

        var prompt = request.message
        val toReturn = mutableMapOf<HandlableMessageType, List<String>>()

        messageHandler.forEach { handler ->
            handler.detectMessagePartType(prompt, request.user.id).ifNotNullOrEmpty {
                logger().info("The following message type was detected in the message with id ${request.id}: ${handler.messagePartType}")

                handler.generateAnswer(it, request.user.id)
                    ?.let { answers -> toReturn.put(handler.messagePartType, answers) }
                prompt = prompt.replaceList(it)
            }
        }

        compilerPipeline.compileMessage(toReturn, request.phoneNumber)
    }
}

