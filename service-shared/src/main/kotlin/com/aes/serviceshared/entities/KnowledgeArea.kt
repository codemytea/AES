package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.Crop
import com.aes.serviceshared.Models.Topic
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