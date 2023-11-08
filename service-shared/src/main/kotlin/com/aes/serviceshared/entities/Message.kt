package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.MessageStatus
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate

@Entity
class Message(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column
    val message_id: Long,

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