package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime


/**
 * Represents a Knowledge Area
 * */
@Entity
@IdClass(KnowledgeAreaId::class)
class KnowledgeArea(

    /**
     * A crop cycle topic
     * */
    @Id
    @Enumerated(EnumType.STRING)
    val topic: Topic = Topic.PESTS,

    /**
     * A crop
     * */
    @Id
    @Enumerated(EnumType.STRING)
    val cropName: Crop = Crop.RICE,

    /**
     * When the knowledge area was last modified
     * */
    @Column
    val modifiedAt: LocalDateTime = LocalDateTime.now()
) {

    override fun equals(other: Any?): Boolean {
        if (other !is KnowledgeArea) return false
        return this.topic == other.topic && this.cropName == other.cropName
    }

    override fun hashCode(): Int {
        return topic.ordinal * 100 + cropName.ordinal
    }
}

data class KnowledgeAreaId(
    val topic: Topic? = null,
    val cropName: Crop? = null,
) : Serializable