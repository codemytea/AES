package com.aes.smsservices.Services

import org.springframework.stereotype.Service
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Models.MessageDTO
import com.aes.smsservices.Models.TranslateMessageDTO
import com.aes.smsservices.Repositories.MessageRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable

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