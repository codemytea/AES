package com.aes.common.Entities

import com.aes.common.Enums.MessageStatus
import com.aes.common.Enums.MessageType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(

    /**
     * Set by system
     * */
    @Id
    var id: Long = 0,

    /**
     * Number message sent to/received from
     * */
    @Column
    var phoneNumber: Long = 0,

    /**
     * Contents of message
     * */
    @Column
    val message: String = "",

    /**
     * When the message was first sent/received
     * */
    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),

    /**
     * Who the message is actually associated with
     * */
    @ManyToOne
    var user: User = User(),

    /**
     * Whether the message was sent by the system or a user
     * */
    @Enumerated(value = EnumType.STRING)
    @Column
    val type: MessageType = MessageType.INCOMING,

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
    val messageTopics: MutableSet<MessageTopics> = mutableSetOf(),

    @Column
    var retried: Int = 0


    )






