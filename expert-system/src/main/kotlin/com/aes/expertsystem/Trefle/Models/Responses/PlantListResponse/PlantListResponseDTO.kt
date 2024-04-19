package com.aes.expertsystem.Trefle.Models.Responses.PlantListResponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantListResponseDTO(
    val data: List<PlantListResponseDataDTO>
)