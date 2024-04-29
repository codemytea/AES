package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlin.reflect.full.memberProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Growth {
    /**
     * The average numbers of days required to from planting to harvest
     */
    var days_to_harvest: Int? = null

    /**
     * A description on how the plant usually grows
     */
    var description: String? = null

    /**
     * A description on how to sow the plant
     */
    var sowing: String? = null

    /**
     * The maximum acceptable soil pH (of the top 30 centimeters of soil) for the plant
     */
    var ph_maximum: Double? = null

    /**
     * The minimum acceptable soil pH (of the top 30 centimeters of soil) for the plant
     */
    var ph_minimum: Double? = null

    /**
     * Required amount of light, on a scale from 0 (no light, <= 10 lux) to 10 (very intensive insolation, >= 100 000 lux)
     */
    var light: Int? = null

    /**
     * Required relative humidity in the air, on a scale from 0 (<=10%) to 10 (>= 90%)
     */
    var atmospheric_humidity: Int? = null

    /**
     * The most active growth months of the species (usually all year round for perennial plants)
     */
    var growth_months: List<Month>? = null

    /**
     * The months the species usually blooms
     */
    var bloom_months: List<Month>? = null

    /**
     * The months the species usually produces fruits
     */
    var fruit_months: List<Month>? = null

    /**
     * The minimum spacing between each rows of plants, in centimeters
     */
    var spacing_between_rows: Cm? = null

    /**
     * The average spreading of the plant, in centimeters
     */
    var average_spreading: Cm? = null

    /**
     * Minimum precipitation per year, in millimeters per year
     */
    var min_precipitation_per_year: Mm? = null

    /**
     * Maximum precipitation per year, in millimeters per year
     */
    var max_precipitation_per_year: Mm? = null

    /**
     * Minimum depth of soil required for the species, in centimeters. Plants that do not have roots such as rootless aquatic plants have 0
     */
    var min_soil_depth: Cm? = null

    /**
     * The minimum tolerable temperature for the species. In Celsius or Fahrenheit degrees
     */
    var min_temperature: Temperature? = null

    /**
     * The maximum tolerable temperature for the species. In Celsius or Fahrenheit degrees
     */
    var max_temperature: Temperature? = null

    /**
     * Required quantity of nutrients in the soil, on a scale from 0 (oligotrophic) to 10 (hypereutrophic)
     */
    var soil_nutriments: Int? = null

    /**
     * Tolerance to salinity, on a scale from 0 (untolerant) to 10 (hyperhaline)
     */
    var soil_salinity: Int? = null

    /**
     * Required texture of the soil, on a scale from 0 (clay) to 10 (rock)
     */
    var soil_texture: Int? = null

    /**
     * Required humidity of the soil, on a scale from 0 (xerophile) to 10 (subaquatic)
     */
    var soil_humidity: Int? = null

    fun mostAreNotNull(): Boolean {
        return this::class.memberProperties.count {
            it.getter.call(this) == null
        } / this::class.memberProperties.size.toFloat() > 0.5
    }
}

/**
 * Enum representing possible months.
 */
enum class Month {
    jan,
    feb,
    mar,
    apr,
    may,
    jun,
    jul,
    aug,
    sep,
    oct,
    nov,
    dec,
}

class Cm(
    val cm: Int?,
)

class Mm(
    val mm: Int?,
)

class Temperature(
    val deg_f: Int?,
    val deg_c: Int?,
)
