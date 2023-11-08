package com.aes.serviceshared.entities

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
class MessageLocalisation(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    val localization_id: UUID,

    @ManyToOne
    @JoinColumn(name = "message_id")
    val message: Message? = null,

    @Column
    val language_code: String? = null,

    @Column
    val translated_content: String? = null
)