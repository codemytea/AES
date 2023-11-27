package com.aes.serviceshared.entities

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @ManyToOne
    val userId: User,

    @Column
    val isTriggered: Boolean,

    @Column
    val isActive: Boolean,

    @OneToOne
    val timedNotifications: TimedNotifications

): Serializable