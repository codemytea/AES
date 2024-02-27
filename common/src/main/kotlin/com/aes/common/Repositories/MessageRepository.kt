package com.aes.common.Repositories

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.MessageType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : CrudRepository<Message, Long> {
    fun getMessageByUserAndTypeOrderByCreatedAt(user: User, type: MessageType = MessageType.INCOMING): List<Message>?

    fun getMessageByUserOrderByCreatedAt(user: User): List<Message>?

    fun isLatestMessageIncomingCollection(user: User): Boolean {
        val lastIncoming = getMessageByUserAndTypeOrderByCreatedAt(user)?.firstOrNull()?.createdAt.also {
            if (it == null) return false
        }

        val lastMessage = getMessageByUserOrderByCreatedAt(user)?.firstOrNull()?.createdAt.also {
            if (it == null) return false
        }

        return !(lastMessage?.isEqual(lastIncoming) == true || lastMessage?.isAfter(lastIncoming) == true)
    }

    fun getMessageByUserIdAndType(userUUID: UUID, type: MessageType = MessageType.INCOMING): List<Message>?
}





