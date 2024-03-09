package com.aes.common.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class Germination(
    val percent_germ: String? = null,
    val presow_treatment: String? = null,
    val temperature: String? = null,
    val light_hours: String? = null,
    val days: String? = null,
    val medium: String? = null,
    val provenance: String? = null,
    val sample_size: Int? = null,
    val reference_id: Int? = null
){
    @Id
    val id: String = UUID.randomUUID().toString()
}