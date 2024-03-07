package com.aes.expertsystem.Models.Responses.PlantDetails

import com.aes.expertsystem.Enums.Dimensions
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Month

@JsonIgnoreProperties(ignoreUnknown = true)
class PlantDetailsResponseDTO (
    val id : Int,
    val common_name : String?,
    val scientific_name : List<String>?,
    val other_name : List<String>?,
    val family : String?,
    val origin: String?,
    val dimensions : Dimensions?,
    val cycle : String?,
    val watering : String?,
    val depth_water_requirement : WaterRequirementDTO?,
    val volume_water_requirement : WaterRequirementDTO?,
    val watering_period : String?,
    val watering_general_benchmark : WaterRequirementDTO?,
    val sunlight : List<String>?,
    val pruning_month : Month?,
    val pruning_count : PruningDTO?,
    val seeds : Int?,
    val attracts : List<String>?,
    val hardiness: HardinessDTO?,
    val flowers : Boolean?,
    val flowering_season : String?,
    val color : String?,
    val soil : List<String>?,
    val pest_suseptability : List<String>??,
    val edible_fruit : Boolean?,
    val growth_rate : String?,
    val maintenance : String?,
    val drought_tolerant : Boolean?,
    val salt_tolerant : Boolean?,
    val invasive : Boolean?,
    val description : String?,

    )