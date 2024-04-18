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
    /**
     * The ID of the notification to be sent
     * */
    @Id
    val id: UUID = UUID.randomUUID(),

    /**
     * When teh notification should be sent
     * */
    val time: LocalDateTime = LocalDateTime.now(),

    /**
     * The user the message is being sent to
     * */
    @ManyToOne
    val user: User = User(),

    /**
     * What crop it has to do with
     * */
    @Enumerated(EnumType.STRING)
    val crop: Crop = Crop.RICE,

    /**
     * What topic it has to do with
     * */
    @Enumerated(EnumType.STRING)
    val topic: Topic = Topic.GROWING,

    /**
     * What the notification is actually about
     * */
    val messagePrompt: String = "",

    /**
     * The message that ends up being sent
     * */
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