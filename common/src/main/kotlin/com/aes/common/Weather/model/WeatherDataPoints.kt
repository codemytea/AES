package com.aes.common.Weather.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDataPoints(
    var time: List<LocalDateTime> = listOf(),
    var temperature_2m: List<Float?>? = null,
    var relative_humidity_2m: List<Float?>? = null,
    var precipitation: List<Float?>? = null,
    var precipitation_probability: List<Float?>? = null,
    var snowfall: List<Float?>? = null,
    var cloud_cover: List<Float?>? = null,
) {
    fun toWeatherInfo(
        lat: Float,
        lng: Float,
    ): List<WeatherInfo> {
        return time.indices.map {
            WeatherInfo(
                time[it],
                lat,
                lng,
                temperature_2m?.get(it),
                relative_humidity_2m?.get(it),
                precipitation?.get(it),
                precipitation_probability?.get(it),
                snowfall?.get(it),
                cloud_cover?.get(it),
            )
        }
    }
}
