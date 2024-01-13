package com.aes.smsservices.Entities

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
class Notification(
    /**
     * A phone number
     * */
    @Id
    val id: Long,

    @ManyToOne
    val userId: User,

    @Column
    val isTriggered: Boolean,

    @Column
    val isActive: Boolean,

    @OneToOne
    val timedNotifications: TimedNotifications

): Serializable