package com.aes.expertsystem.Models.Responses.PlantDisease

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantDiseaseResponseDataDTO (
    val id : Int,
    val common_name : String?,
    val scientific_name : String?,
    val other_name : List<String>?,
    val family : String?,
    val description : String?,
    val solution : String?,
    val host : List<String>?,
)