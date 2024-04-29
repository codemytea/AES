package com.aes.expertsystem.Data.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class SeedWeight(
    val thousandseedweight: Float? = null,
    @Column(length = 1000)
    val notes: String? = null,
    val reference_id: Int? = null,
    @Embedded
    val material_weighed: MaterialWeighed? = null,
) {
    @Id
    val id: String = UUID.randomUUID().toString()
}
