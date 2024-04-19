package com.aes.expertsystem.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class Morphology(
    val species_id: Int? = null,
    val fruit_size_length: String? = null,
    val fruit_size_width: String? = null,
    val fruit_size_thick: String? = null,
    val diaspore: String? = null,
    val mechanical_protection_of_seed: String? = null,
    val fruit_dehiscent: Int? = null,
    val fruit_indehiscent: Int? = null,
    val fruit_remarks: String? = null,
    val seed_size_length: String? = null,
    val seed_size_width: String? = null,
    val seed_size_height: String? = null,
    val seed_size_remarks: String? = null,
    val seed_colour: String? = null,
    val seed_shape: String? = null,
    val sarcotesta: String? = null,
    val seed_appendages: String? = null,
    val seed_surface: String? = null,
    val raphe: String? = null,
    val other_specialised_structures: String? = null,
    val diaspore_size_length: String? = null,
    val diaspore_size_thickness: String? = null,
    val diaspore_size_width: String? = null,
    val diaspore_size_remarks: String? = null,
    val diaspore_colour: String? = null,
    val diaspore_shape: String? = null,
    val diaspore_surface: String? = null,
    val dispersal_aids: String? = null,
    @Column(length = 1000)
    val notes: String? = null,
    val seed_configuration: String? = null,
    val embryo_type: String? = null,
    val relative_size_embryo: String? = null,
    val perisperm_present: String? = null,
    val storage_remarks: String? = null,
    val endosperm_ruminate: String? = null,
    val embryo_colour: String? = null,
    val reference_id: Int? = null
){
    @Id
    val id: String = UUID.randomUUID().toString()
}