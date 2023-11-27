package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Message
import com.aes.serviceshared.entities.MessageTopics
import com.aes.serviceshared.entities.MessageTopicsId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageTopicsRepository: CrudRepository<MessageTopics, MessageTopicsId> {
}