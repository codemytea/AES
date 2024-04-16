package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Repositories.MessageRepository
import com.aes.common.logging.Logging
import com.aes.smsservices.Exceptions.ResourceNotFoundException
import com.aes.smsservices.Models.MessageStatusDTO
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UpdateSmsService(
    val messageRepository: MessageRepository
) : Logging {

    /**
     * Updates the given status of a message.
     * */
    @Transactional
    fun update(resource: MessageStatusDTO): Message {
        val toUpdate = messageRepository.findByIdOrNull(resource.id) ?: throw ResourceNotFoundException(resource.id)

        toUpdate.status = resource.status

        return toUpdate
    }
}