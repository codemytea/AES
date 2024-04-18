package com.aes.common.CropGroup.entities

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

    @Id
    val subgroupLetter: String = "",

    @Id
    val groupNumber: Int = 0,

    val name: String = "",

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