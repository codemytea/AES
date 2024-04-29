package com.aes.common.Repositories

import com.aes.common.Entities.MessageTopics
import com.aes.common.Entities.MessageTopicsId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageTopicsRepository : CrudRepository<MessageTopics, MessageTopicsId>
