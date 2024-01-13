package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Entities.MessageTopics
import com.aes.smsservices.Entities.MessageTopicsId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageTopicsRepository: CrudRepository<MessageTopics, MessageTopicsId> {
}