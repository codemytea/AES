package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.MessageType
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
class UserMessagesContextProvider: InitialContextProvider {

    fun Message.toContext(index: Int): String?{
        return when(type){
            MessageType.INCOMING -> "User sent message $index: \"$message\""
            MessageType.OUTGOING -> "System sent message $index: \"$message\""
            else -> null
        }
    }
    override fun contextForMessage(message: String, user: User): List<String> {
        return user.messages.sortedBy { it.createdAt }.mapIndexedNotNull { index, m ->
            m.toContext(index)
        } + listOf("You are System")
    }
}