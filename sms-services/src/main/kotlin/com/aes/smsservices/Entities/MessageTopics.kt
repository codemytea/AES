package com.aes.smsservices.Entities

import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(MessageTopicsId::class)
class MessageTopics(
    @Id
    @ManyToOne
    val knowledgeArea: KnowledgeArea
){
    @Id
    @ManyToOne
    val sms: Message? = null
}

data class MessageTopicsId(
    val sms: Message? = null,
    val knowledgeArea: KnowledgeArea? = null
): Serializable