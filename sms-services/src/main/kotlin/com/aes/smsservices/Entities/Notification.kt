package com.aes.smsservices.Entities

import com.aes.common.Entities.User
import jakarta.persistence.*
import java.io.Serializable

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