package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@IdClass(UserKnowledgeId::class)
class UserKnowledge(

    /**
     * The user
     * */
    @Id
    @ManyToOne
    val user: User? = null,

    /**
     * The users knowledge about this are (0-1)
     * */
    @Column
    val knowledgeLevel: Double? = null,

    /**
     * The topic (part of the crop cycle) the knowledge area refers to
     * */
    @Id
    @Enumerated(value = EnumType.STRING)
    val topic: Topic = Topic.GROWING,

    /**
     * The crop the knowledge area refers to
     * */
    @Id
    @Enumerated(value = EnumType.STRING)
    val crop: Crop = Crop.RICE

)

data class UserKnowledgeId(
    val user: User? = null,
    val topic: Topic = Topic.GROWING,
    val crop: Crop = Crop.RICE,
) : Serializable