package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.MessageStatus
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.util.*

@Entity
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    val message_id: UUID,

    @Column
    val content: String? = null,

    @Column
    val timestamp: LocalDate? = null,

    @Column
    val is_sent_by_user: Boolean? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val status: MessageStatus? = null, //PENDING, FAILED, DELIVERED etc

    @Column
    val sent_at: LocalDate? = null
)