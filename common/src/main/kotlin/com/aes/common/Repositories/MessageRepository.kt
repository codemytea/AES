package com.aes.common.Repositories

import com.aes.common.Entities.Message
import com.aes.common.Enums.MessageType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : CrudRepository<Message, Long> {
    fun getMessageByUserIdAndType(userUUID: UUID, type: MessageType = MessageType.INCOMING): List<Message>?
}





