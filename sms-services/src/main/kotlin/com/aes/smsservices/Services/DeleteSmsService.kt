package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DeleteSmsService(
    val messageRepository: MessageRepository
) : Logging {

    /**
     * Deletes message stored in system using its id
     *
     * @param resourceId
     *
     * @return the message one last time
     * */
    @Transactional
    fun deleteSMS(resourceId: Long): Message? {
        logger().info("Deleting message with ID $resourceId")

        val temp = messageRepository.findByIdOrNull(resourceId)

        messageRepository.deleteById(resourceId)

        return temp
    }
}