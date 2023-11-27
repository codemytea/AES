package com.aes.serviceshared.Models

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

class MessageDTO(
    val message_id: UUID,

    val content: String? = null,

    val timestamp: LocalDate? = null,

    val is_sent_by_user: Boolean? = null,

    val status: MessageStatus? = null, //PENDING, FAILED, DELIVERED etc

    val sent_at: LocalDate? = null
)



