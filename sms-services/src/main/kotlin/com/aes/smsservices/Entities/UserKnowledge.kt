package com.aes.smsservices.Entities

import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(UserKnowledgeId::class)
class UserKnowledge(

    @Id
    @ManyToOne
    val user: User,

    @Column
    val knowledgeLevel: Int?,

    @Id
    @ManyToOne
    val knowledgeArea: KnowledgeArea

    )

data class UserKnowledgeId(
    val user: User? = null,
    val knowledgeArea: KnowledgeArea? = null
): Serializable