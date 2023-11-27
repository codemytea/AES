package com.aes.serviceshared.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.ManyToOne
import java.io.Serializable

@Entity
@IdClass(NotificationTopicsId::class)
class NotificationTopics (
    @Id
    @ManyToOne
    val notification: Notification,

    @Id
    @ManyToOne
    val knowledgeArea: KnowledgeArea
)

data class NotificationTopicsId(
    val notification: Notification? = null,
    val knowledgeArea: KnowledgeArea? = null
): Serializable