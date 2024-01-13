package com.aes.smsservices.Services

import com.aes.common.logging.Logging
import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Exceptions.ResourceNotFoundException
import com.aes.smsservices.Models.MessageStatusDTO
import com.aes.smsservices.Repositories.MessageRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UpdateSmsService(
    val messageRepository: MessageRepository
) : Logging {

    @Transactional
    fun update(resource: MessageStatusDTO) : Message{
        val toUpdate = messageRepository.findByIdOrNull(resource.id)?: throw ResourceNotFoundException(resource.id)

        toUpdate.status = resource.status

        return toUpdate
    }
}