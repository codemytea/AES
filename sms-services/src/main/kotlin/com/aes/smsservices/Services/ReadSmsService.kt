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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

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