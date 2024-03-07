package com.aes.common.Notification.entities

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Models.MessageDTO
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


@Entity
class ScheduledNotification(
    @Id
    val id: String,
    val time: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val crop: Crop,

    @Enumerated(EnumType.STRING)
    val topic: Topic,

    @ManyToOne
    val user: User,

    val messagePrompt: String,

    @OneToOne
    val associatedMessage: Message? = null
){
    fun toReceivedSMS(): MessageDTO{
        return MessageDTO(
            UUID.randomUUID().mostSignificantBits,
            user.id,
            messagePrompt,
            user.phoneNumber.first()
        )
    }
}