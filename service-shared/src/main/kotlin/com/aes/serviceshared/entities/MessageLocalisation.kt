package com.aes.serviceshared.entities

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
class MessageLocalisation(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column
    val localization_id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "message_id")
    val message: Message? = null,

    @Column
    val language_code: String? = null,

    @Column
    val translated_content: String? = null
)