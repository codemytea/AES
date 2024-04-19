package com.aes.common

import com.aes.common.Weather.geocoding.GeocodingService
import com.aes.common.Weather.provider.WeatherService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class WeatherAPITests {

    val weatherService: WeatherService = WeatherService(GeocodingService())

    @Test
    fun weatherFetchesCorrectly() {
        val result = weatherService.getWeatherForDateAtLocation("London", "United States", LocalDateTime.now())
        println(jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
        }.writerWithDefaultPrettyPrinter().writeValueAsString(result))
    }
}