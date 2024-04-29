package com.aes.messagehandler.Services

import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Models.MessageQueueItem
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.messagecompiler.Controller.CompilerPipeline
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Utilities.ifNotNullOrEmpty
import com.aes.messagehandler.Utilities.replaceList
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessageHandlerService(
    private val messageHandler: List<MessageHandler>,
    private val compilerPipeline: CompilerPipeline,
    private val messageRepository: MessageRepository,
) : Logging {
    /**
     * Sends a list of responses that need to be tailored and what type there are
     * to the compiler pipeline.
     * Leverages spring boot @Order to make it easily extensible.
     *
     * @param message - the incoming message
     * */
    @Transactional
    fun handleRequest(message: MessageQueueItem) {
        val request = messageRepository.findByIdOrNull(message.messageId)!!
        var prompt = request.message
        val toReturn = mutableMapOf<HandlableMessageType, List<String>>()

        messageHandler.forEach { handler ->
            handler.extractPartAndReturn(prompt, request.user.id).ifNotNullOrEmpty {
                prompt = prompt.replaceList(it)
                handler.generateAnswer(it, request.user.id)?.let { answers ->
                    toReturn.put(handler.messagePartType, answers)
                }
            }
        }

        compilerPipeline.compileMessage(toReturn, request.phoneNumber)
    }
}
