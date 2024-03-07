package com.aes.expertsystem.Models.Responses.PlantList

class PlantListResponseDTO (
    val data : List<PlantListResponseDataDTO>,
    val to : Int,
    val per_page : Int,
    val current_page : Int,
    val from : Int,
    val last_page : Int,
    val total : Int
)