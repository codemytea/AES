package com.aes.expertsystem.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Embeddable

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
class DispersalAgent(
    val description: String? = null
)