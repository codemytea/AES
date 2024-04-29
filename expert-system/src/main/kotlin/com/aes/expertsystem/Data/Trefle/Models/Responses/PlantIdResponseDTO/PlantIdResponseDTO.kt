package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantIdResponseDTO(
    val data: PlantIdResponseDataDTO,
)
