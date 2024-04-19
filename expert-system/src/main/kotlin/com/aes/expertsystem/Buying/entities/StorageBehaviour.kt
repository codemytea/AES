package com.aes.expertsystem.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class StorageBehaviour(
    val id: Int = 0,
    val species_id: Int = 0,
    val distribution: String? = null,

    @Column(length = 100000)
    val storage_behaviour: String? = null,
    @Column(length = 100000)
    val storage_conditions: String? = null
) {
    @Id
    val storage_id: String = UUID.randomUUID().toString()
}