package com.aes.expertsystem.Data.CropGroup.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.*


@Entity
class CropGroupEntry(
    /**
     * The name of the Crop
     * */
    val name: String = "",

    /**
     * The crops sub-group
     * */
    @ManyToOne
    val cropSubGroup: CropGroupEntity? = null
) {

    @Id
    val id: UUID = UUID.randomUUID()

    override fun equals(other: Any?): Boolean {
        return (other as? CropGroupEntry)?.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}