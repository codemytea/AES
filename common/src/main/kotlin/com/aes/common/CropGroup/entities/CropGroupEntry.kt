package com.aes.common.CropGroup.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.*


@Entity
class CropGroupEntry(
    val name: String = "",

    @ManyToOne
    val cropSubGroup: CropGroupEntity? = null
) {

    @Id
    val id: String = UUID.randomUUID().toString()
    override fun equals(other: Any?): Boolean {
        return (other as? CropGroupEntry)?.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}