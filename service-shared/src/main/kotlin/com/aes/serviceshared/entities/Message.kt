package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.LanguageCode
import com.aes.serviceshared.Models.MessageStatus
import com.aes.serviceshared.Models.MessageType
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class Message(

    @Column
    val message: String,

    @Column
    val sentAt: LocalDateTime,

    @Enumerated(value = EnumType.STRING)
    @Column
    val type: MessageType,

    @Enumerated(value = EnumType.STRING)
    @Column
    val status: MessageStatus,

    @Enumerated(value = EnumType.STRING)
    @Column
    val language: LanguageCode,

    @OneToMany
    val messageTopics: List<MessageTopics> = listOf()
    )
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @ManyToOne
    var user: User? = null

}





