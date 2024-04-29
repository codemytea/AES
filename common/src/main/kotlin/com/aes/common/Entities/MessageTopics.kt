package com.aes.common.Entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.ManyToOne
import java.io.Serializable

@Entity
@IdClass(MessageTopicsId::class)
class MessageTopics(
    /**
     * The knowledge areas associated to a given message
     * */
    @Id
    @ManyToOne
    val knowledgeArea: KnowledgeArea = KnowledgeArea(),
    /**
     * The message
     * */
    @Id
    @ManyToOne
    val sms: Message = Message(),
)

data class MessageTopicsId(
    val sms: Message = Message(),
    val knowledgeArea: KnowledgeArea = KnowledgeArea(),
) : Serializable
