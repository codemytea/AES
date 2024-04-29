package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Flower(
    /**
     * The flower colour(s)
     */
    var color: List<FlowerColor>? = null,
    /**
     * Is the flower visible?
     */
    var conspicuous: Boolean? = null,
)

/**
 * Enum representing possible flower colours.
 */
enum class FlowerColor {
    white,
    red,
    brown,
    orange,
    yellow,
    lime,
    green,
    cyan,
    blue,
    purple,
    magenta,
    grey,
    black,
}
