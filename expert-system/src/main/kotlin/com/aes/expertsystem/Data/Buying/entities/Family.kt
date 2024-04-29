package com.aes.expertsystem.Data.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Embeddable

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
class Family(
    val name: String = "",
)
