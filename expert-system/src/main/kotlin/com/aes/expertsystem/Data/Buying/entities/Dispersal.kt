package com.aes.expertsystem.Data.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class Dispersal(
    @Column(length = 1000)
    val notes: String? = null,
    val method: String? = null,
    val animal_group: String? = null,
    val animal_species: String? = null,
    val reference_id: Int? = null,
    @Embedded
    val dispersal_agents: DispersalAgent? = null,
) {
    @Id
    val id: String = UUID.randomUUID().toString()
}
