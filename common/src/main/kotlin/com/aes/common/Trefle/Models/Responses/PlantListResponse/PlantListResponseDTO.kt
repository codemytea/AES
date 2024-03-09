package com.aes.common.Trefle.Models.Responses.PlantListResponse

import com.aes.common.Trefle.Models.Responses.PlantListResponse.PlantListResponseDataDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantListResponseDTO (
    val data : List<PlantListResponseDataDTO>
)