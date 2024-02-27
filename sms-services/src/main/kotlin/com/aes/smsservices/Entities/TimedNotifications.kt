package com.aes.smsservices.Entities

import com.aes.common.Enums.TimeIdentifiers
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class TimedNotifications(

    @OneToOne
    val notification: Notification,

    @Column
    val firstSetAt: LocalDateTime? = null,

    @Column
    val prompt: String? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val notificationFrequency: TimeIdentifiers? = null

) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID
}