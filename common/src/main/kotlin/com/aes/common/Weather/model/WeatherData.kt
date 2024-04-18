package com.aes.common.Weather.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherData(
    var latitude: Float,
    var longitude: Float,

    @JsonAlias("hourly")
    var weatherData: WeatherDataPoints
) {

    fun toWeatherInfo(lat: Float, lng: Float): List<WeatherInfo> {
        return weatherData.toWeatherInfo(lat, lng)
    }
}