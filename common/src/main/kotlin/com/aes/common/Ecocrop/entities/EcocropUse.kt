package com.aes.common.Ecocrop.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class EcocropUse(
    val useType: String? = null,
    val detailedUse: String? = null,
    val usePart: String? = null
) {
    @Id
    val id: String = UUID.randomUUID().toString()
}