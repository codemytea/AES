package com.aes.common.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class SourcesDTO (

    val self : String?,
    val genus : String?,
    val species : String?

    )