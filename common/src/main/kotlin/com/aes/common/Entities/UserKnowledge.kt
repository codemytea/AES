package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@IdClass(UserKnowledgeId::class)
class UserKnowledge(

    @Id
    val userId: UUID = UUID.randomUUID(),

    @Column
    val knowledgeLevel: Double? = null,

    @Id
    @Enumerated(value = EnumType.STRING)
    val topic: Topic = Topic.GROWING,

    @Id
    @Enumerated(value = EnumType.STRING)
    val crop: Crop = Crop.RICE

)

data class UserKnowledgeId(
    val userId: UUID? = UUID.randomUUID(),
    val topic: Topic = Topic.GROWING,
    val crop: Crop = Crop.RICE,
) : Serializable