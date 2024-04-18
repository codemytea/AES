package com.aes.common.Weather.provider

import com.aes.common.Weather.geocoding.GeocodingService
import com.aes.common.Weather.model.WeatherData
import com.aes.common.Weather.model.WeatherDataPoints
import com.aes.common.Weather.model.WeatherInfo
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class WeatherService(
    val geocodingService: GeocodingService
) {

    companion object {
        private val variables = WeatherDataPoints::class.java
            .declaredFields
            .map { it.name }
            .toMutableList()
            .also { it.remove("time") }
    }

    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder().build().apply {
            interceptors.add { request, body, execution ->
                println(request.uri.toString())
                execution.execute(request, body)
            }
        }
    }

    /**
     * Creates the correct URL for querying historical data
     * */
    private fun historicalUrl(
        lat: Float,
        lng: Float,
        startDate: LocalDate,
        endDate: LocalDate,
        variables: List<String>
    ): String {
        fun LocalDate.formatted(): String {
            return this.format(DateTimeFormatter.ISO_DATE)
        }
        return "https://archive-api.open-meteo.com/v1/era5?latitude=$lat&longitude=$lng&start_date=${startDate.formatted()}&end_date=${endDate.formatted()}${
            variables.joinToString(
                ""
            ) { "&hourly=$it" }
        }"
    }

    /**
     * Creates the correct URL for querying forcasts
     * */
    private fun forecastUrl(
        lat: Float,
        lng: Float,
        startDate: LocalDate,
        endDate: LocalDate,
        variables: List<String>
    ): String {
        fun LocalDate.formatted(): String {
            return this.format(DateTimeFormatter.ISO_DATE)
        }
        return "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lng&start_date=${startDate.formatted()}&end_date=${endDate.formatted()}${
            variables.joinToString(
                ""
            ) { "&hourly=$it" }
        }"
    }

    /**
     * Gets past weather for a specific location at a certain date
     * */
    private fun getHistoricalWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo> {
        return restTemplate().getForEntity(
            historicalUrl(lat, lng, date.toLocalDate(), date.toLocalDate(), variables),
            WeatherData::class.java
        ).body!!.toWeatherInfo(lat, lng)
    }

    /**
     * Gets the weather forecast for a certain date
     * */
    private fun getForecastWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo> {
        return restTemplate().getForEntity(
            forecastUrl(lat, lng, date.toLocalDate(), date.toLocalDate(), variables),
            WeatherData::class.java
        ).body!!.toWeatherInfo(lat, lng)
    }

    /**
     * Gets the weather forecast between two dates
     * */
    private fun getForecastWeatherBetweenDates(
        lat: Float,
        lng: Float,
        from: LocalDate,
        to: LocalDate
    ): List<WeatherInfo> {
        val result = mutableListOf<WeatherInfo>()
        if (from.isBefore(LocalDate.now().plusWeeks(2))) {
            result += restTemplate().getForEntity(
                forecastUrl(lat, lng, from, minOf(to, LocalDate.now().plusWeeks(2)), variables),
                WeatherData::class.java
            ).body!!.toWeatherInfo(lat, lng)
        }
        if (to.isAfter(LocalDate.now().plusWeeks(2))) {
            result += getHistoricalWeatherBetweenDates(
                lat,
                lng,
                LocalDate.now().plusWeeks(2).minusYears(1),
                to.minusYears(1)
            )
        }
        return result
    }

    /**
     * Gets the past weather between two dates in the past
     * */
    private fun getHistoricalWeatherBetweenDates(
        lat: Float,
        lng: Float,
        from: LocalDate,
        to: LocalDate
    ): List<WeatherInfo> {
        return restTemplate().getForEntity(
            historicalUrl(lat, lng, from, to, variables),
            WeatherData::class.java
        ).body!!.toWeatherInfo(lat, lng)
    }

    private fun getWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo> {
        return if (date.isBefore(LocalDateTime.now().minusDays(5))) {
            getHistoricalWeatherForDate(lat, lng, date)
        } else {
            getForecastWeatherForDate(lat, lng, date)
        }
    }

    fun getWeatherForDateAtLocation(cityName: String, countryName: String, date: LocalDateTime): WeatherInfo {
        val location = geocodingService.getLatLngForName(cityName, countryName)
        return getWeatherForDate(location.latitude, location.longitude, date).first {
            it.time.toEpochSecond(ZoneOffset.UTC) == date.withMinute(0).withSecond(0).toEpochSecond(ZoneOffset.UTC)
        }
    }

    fun getWeatherBetweenDatesAtLocation(
        cityName: String,
        countryName: String,
        from: LocalDate,
        to: LocalDate
    ): List<WeatherInfo> {
        val location = geocodingService.getLatLngForName(cityName, countryName)

        val result = mutableListOf<WeatherInfo>()

        if (from.isBefore(LocalDate.now().minusDays(5))) {
            result += getHistoricalWeatherBetweenDates(
                location.latitude,
                location.longitude,
                from,
                minOf(LocalDate.now().minusDays(5), to)
            )
            if (to.isAfter(LocalDate.now().minusDays(5))) {
                result += getForecastWeatherBetweenDates(
                    location.latitude,
                    location.longitude,
                    LocalDate.now().minusDays(5),
                    to
                )
            }
        } else {
            result += getForecastWeatherBetweenDates(location.latitude, location.longitude, from, to)
        }
        return result
    }

    /**
     * Used for planting calculations - check when all future dates match a condition eg
     * If you want to plant corn, you need it at a point when there in no risk of frost
     * so use this function to determine when that will be
     * */
    fun allFutureDatesMatchCondition(
        from: LocalDate,
        to: LocalDate,
        cityName: String,
        countryName: String,
        condition: (WeatherInfo) -> Boolean
    ): Boolean {
        return getWeatherBetweenDatesAtLocation(cityName, countryName, from, to).all(condition)
    }
}