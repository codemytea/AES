package com.aes.smsservices.Entities

import com.aes.smsservices.Enums.Crop
import com.aes.smsservices.Enums.Topic
import jakarta.persistence.*
import java.io.Serializable


@Entity
@IdClass(KnowledgeAreaId::class)
class KnowledgeArea(

    @Id
    val topic : Topic,

    @Id
    val cropName : Crop,
)

data class KnowledgeAreaId(
    val topic : Topic? = null,
    val cropName: Crop? = null,
) : Serializable