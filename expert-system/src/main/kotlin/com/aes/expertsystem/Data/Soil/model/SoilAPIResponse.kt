package com.aes.expertsystem.Data.Soil.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SoilAPIResponse(
    @JsonAlias("wrb_class_name") val mostProbableSoilType: SoilType,
)
