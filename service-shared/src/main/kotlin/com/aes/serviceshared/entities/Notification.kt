package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.NotificationTypes
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    val notification_id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val notification_type: NotificationTypes? = null,
)