package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.MessageType
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class UserMessagesContextProvider: InitialContextProvider {

    fun Message.toContext(index: Int): String?{
        return when(type){
            MessageType.INCOMING -> "User sent message $index: \"$message\" at ${createdAt.format(DateTimeFormatter.ISO_DATE_TIME)}"
            MessageType.OUTGOING -> "System sent message $index: \"$message\" at ${createdAt.format(DateTimeFormatter.ISO_DATE_TIME)}"
            else -> null
        }
    }
    override fun contextForMessage(message: String, user: User): List<String> {
        return user.messages.sortedBy { it.createdAt }.mapIndexedNotNull { index, m ->
            m.toContext(index)
        } + listOf("You are System")
    }
}