package com.aes.expertsystem.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class FruitOrSeed(

    /**
     * Is the fruit visible?
     */
    var conspicuous: Boolean? = null,

    /**
     * The fruit colour(s)
     */
    var color: List<FruitColor>? = null,

    /**
     * Fruit shape
     */
    var shape: String? = null,

    /**
     * Are the fruit or seed generally recognized as being persistent on the plant?
     */
    var seed_persistence: Boolean? = null
)

/**
 * Enum representing possible fruit colours.
 */
enum class FruitColor {
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
    black
}