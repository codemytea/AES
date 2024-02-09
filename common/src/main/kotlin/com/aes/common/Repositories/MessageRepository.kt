package com.aes.common.Repositories

import com.aes.common.Entities.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository: CrudRepository<Message, Long>{
    fun getMessagesByUserId(userUUID: UUID): List<Message>
    fun getMessageById(id: Long): Message?
}





