package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantListResponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantListResponseDTO(
    val data: List<PlantListResponseDataDTO>
)