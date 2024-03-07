package com.aes.expertsystem.Models.Responses.PlantDisease

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantDiseaseResponseDTO (
    val data : PlantDiseaseResponseDataDTO,
)