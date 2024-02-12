package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(UserKnowledgeId::class)
class UserKnowledge(

    @Id
    @ManyToOne
    val user: User = User(),

    @Column
    val knowledgeLevel: Double? = null,

    @Id
    @Enumerated(value = EnumType.STRING)
    val topic: Topic? = null,

    @Id
    @Enumerated(value = EnumType.STRING)
    val crop: Crop? = null

    )

data class UserKnowledgeId(
    val user: User,
    val topic: Topic? = null,
    val crop: Crop? = null,
): Serializable