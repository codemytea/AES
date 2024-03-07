package com.aes.common.Weather.model

import java.time.LocalDateTime

class WeatherInfo(
    var time: LocalDateTime,
    var lat: Float,
    var lng: Float,
    var temperature: Float?,
    var humidity: Float?,
    var precipitation: Float?,
    var precipitationProbability: Float?,
    var snowfallCm: Float?,
    var cloudCoverPercentage: Float?
)