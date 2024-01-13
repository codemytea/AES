package com.aes.smsservices.Entities

import com.aes.smsservices.Enums.MessageStatus
import com.aes.smsservices.Enums.MessageType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(

    /**
     * Set by system
     * */
    @Id
    var id: Long,

    /**
     * Number message sent to/received from
     * */
    @Column
    var phoneNumber: Long,

    /**
     * Contents of message
     * */
    @Column
    val message: String,

    /**
     * When the message was first sent/received
     * */
    @Column
    val createdAt: LocalDateTime,

    /**
     * Who the message is actually associated with
     * */
    @ManyToOne
    var user: User,

    /**
     * Whether the message was sent by the system or a user
     * */
    @Enumerated(value = EnumType.STRING)
    @Column
    val type: MessageType,

    /**
     * When the message was last updated
     * */
    @Column
    val updatedAt: LocalDateTime? = null,

    /**
     * What the status of the message currently is eg PENDING, DELIVERED etc
     * Only status if incoming
     * */
    @Enumerated(value = EnumType.STRING)
    @Column
    var status: MessageStatus? = null,

    /**
     * The calculated message topics
     * */
    @OneToMany
    val messageTopics: List<MessageTopics>? = null,


)






