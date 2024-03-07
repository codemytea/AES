package com.aes.expertsystem.Models.Responses.PlantList

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantListResponseDataDTO (
    val id : Int,
    val common_name: String,
    val scientific_name: List<String>,
    val other_name: List<String>,
    val cycle : String,
    val watering : String,
    val sunlight : List<String>,
    )