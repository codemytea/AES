package com.aes.expertsystem.CropGroup.entities

import com.aes.common.Enums.CropGroup
import jakarta.persistence.*
import java.io.Serializable

data class CropGroupId(
    val subgroupLetter: String = "",
    val groupNumber: Int = 0
) : Serializable

@Entity
@IdClass(CropGroupId::class)
class CropGroupEntity(

    /**
     * Sub-group crop letter as pert IR-4
     * */
    @Id
    val subgroupLetter: String = "",

    /**
     * Group Crop letter as per IR-4
     * */
    @Id
    val groupNumber: Int = 0,

    /**
     * The name of the crop group
     * */
    val name: String = "",

    /**
     * A list of which crops are part of what crop group
     * */
    @OneToMany
    @JoinColumns(
        JoinColumn(name = "crop_sub_group_group_number", referencedColumnName = "groupNumber"),
        JoinColumn(name = "crop_sub_group_subgroup_letter", referencedColumnName = "subgroupLetter")
    )
    val entries: MutableSet<CropGroupEntry> = mutableSetOf()
) {

    override fun equals(other: Any?): Boolean {
        return (other as? CropGroupEntity)?.let {
            it.subgroupLetter == subgroupLetter && it.groupNumber == groupNumber
        } == true
    }

    override fun hashCode(): Int {
        return subgroupLetter.hashCode() + groupNumber.hashCode()
    }

    fun isMain(): Boolean {
        return subgroupLetter.isBlank()
    }

    fun getCropGroup(): CropGroup {
        return CropGroup.values().first { it.dbName == name }
    }
}