package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.NotificationTypes
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
class Notification(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column
    val notification_id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val notification_type: NotificationTypes? = null,
)