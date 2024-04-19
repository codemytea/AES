package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class Foliage(
    /**
     * The general texture of the plant’s foliage
     */
    var texture: FoliageTexture? = null,

    /**
     * The leaves color(s)
     */
    var color: List<LeafColor>? = null,

    /**
     * Does the leaves stay all year long?
     */
    var leaf_retention: Boolean? = null
) {
    // Additional methods can be added here if needed
}

/**
 * Enum representing possible foliage textures.
 */
enum class FoliageTexture {
    fine,
    medium,
    coarse
}

/**
 * Enum representing possible leaf colors.
 */
enum class LeafColor {
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