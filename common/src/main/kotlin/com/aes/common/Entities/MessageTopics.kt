package com.aes.common.Entities

import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(MessageTopicsId::class)
class MessageTopics(
    @Id
    @ManyToOne
    val knowledgeArea: KnowledgeArea = KnowledgeArea(),

    @Id
    @ManyToOne
    val sms: Message = Message()
)

data class MessageTopicsId(
    val sms: Message = Message(),
    val knowledgeArea: KnowledgeArea = KnowledgeArea()
): Serializable