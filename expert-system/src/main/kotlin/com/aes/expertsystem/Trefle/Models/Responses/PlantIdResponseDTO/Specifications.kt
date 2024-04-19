package com.aes.expertsystem.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class Specifications(
    /**
     * The ligneous type of the woody plant
     */
    var ligneous_type: LigneousType? = null,

    /**
     * The primary growth form on the landscape in relation to soil stabilization on slopes and streamsides? Each plant species is assigned the single growth form that most enhances its ability to stabilize soil
     */
    var growth_form: String? = null,

    /**
     * The general appearance, growth form, or architecture of the plant
     */
    var growth_habit: String? = null,

    /**
     * The relative growth speed of the plant
     */
    var growth_rate: String? = null,

    /**
     * The average height of the species, in centimeters
     */
    var average_height: Height? = null,

    /**
     * The maximum height of the species, in centimeters
     */
    var maximum_height: Height? = null,

    /**
     * Capability to fix nitrogen in monoculture
     */
    var nitrogen_fixation: String? = null,

    /**
     * The predominant shape of the species
     */
    var shape_and_orientation: String? = null,

    /**
     * Relative toxicity of the species for humans or animals
     */
    var toxicity: ToxicityLevel? = null
)

/**
 * Enum representing possible ligneous types.
 */
enum class LigneousType {
    LIANA,
    SUBSHRUB,
    SHRUB,
    TREE,
    PARASITE
}

/**
 * Enum representing possible toxicity levels.
 */
enum class ToxicityLevel {
    NONE,
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Class representing height.
 */
class Height(
    /**
     *  Height of the species, in centimeters
     * */
    var cm: Int
)