package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReadSmsService(
    val messageRepository: MessageRepository
) : Logging {

    /**
     * Gets message from message id
     *
     * @param resourceId
     * @return the message
     * */
    @Transactional
    fun getSMS(resourceId: Long): Message? {
        logger().info("Getting message with ID $resourceId")

        return messageRepository.findByIdOrNull(resourceId)
    }
}