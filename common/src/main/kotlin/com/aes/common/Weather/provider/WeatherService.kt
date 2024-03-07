package com.aes.common.Weather.provider

import com.aes.common.Weather.geocoding.GeocodingService
import com.aes.common.Weather.model.WeatherData
import com.aes.common.Weather.model.WeatherDataPoints
import com.aes.common.Weather.model.WeatherInfo
import kotlinx.coroutines.runBlocking
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.logging.Logger

@Service
class WeatherService(
    val geocodingService: GeocodingService
) {


    companion object {
        private val variables = WeatherDataPoints::class.java
            .declaredFields
            .map { it.name }
            .toMutableList()
            .also{it.remove("time")}
    }

    fun restTemplate(): RestTemplate{
        return RestTemplateBuilder().build().apply {
            interceptors.add { request, body, execution ->
                println(request.uri.toString())
                execution.execute(request, body)
            }
        }
    }

    private fun historicalUrl(lat: Float, lng: Float, startDate: LocalDate, endDate: LocalDate, variables: List<String>):String{
        fun LocalDate.formatted(): String{
            return this.format(DateTimeFormatter.ISO_DATE)
        }
        return "https://archive-api.open-meteo.com/v1/era5?latitude=$lat&longitude=$lng&start_date=${startDate.formatted()}&end_date=${endDate.formatted()}${variables.joinToString("") { "&hourly=$it" }}"
    }

    private fun forecastUrl(lat: Float, lng: Float, startDate: LocalDate, endDate: LocalDate, variables:List<String>): String{
        fun LocalDate.formatted(): String{
            return this.format(DateTimeFormatter.ISO_DATE)
        }
        return "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lng&start_date=${startDate.formatted()}&end_date=${endDate.formatted()}${variables.joinToString("") { "&hourly=$it" }}"

    }


    private fun getHistoricalWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo>{
        return restTemplate().getForEntity(
            historicalUrl(lat, lng, date.toLocalDate(), date.toLocalDate(), variables),
            WeatherData::class.java
        ).body!!.toWeatherInfo(lat, lng)
    }

    private fun getForecastWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo>{
        return restTemplate().getForEntity(
            forecastUrl(lat, lng, date.toLocalDate(), date.toLocalDate(), variables),
            WeatherData::class.java
        ).body!!.toWeatherInfo(lat, lng)
    }

    private fun getWeatherForDate(lat: Float, lng: Float, date: LocalDateTime): List<WeatherInfo> {
        return runBlocking {
            if(date.isBefore(LocalDateTime.now().minusDays(5))){
                getHistoricalWeatherForDate(lat, lng, date)
            } else{
                getForecastWeatherForDate(lat, lng, date)
            }
        }

    }

    fun getWeatherForDateAtLocation(locationName: String, date: LocalDateTime): WeatherInfo{
        val location = geocodingService.getLatLngForName(locationName)
        return getWeatherForDate(location.latitude, location.longitude, date).first {
            it.time.toEpochSecond(ZoneOffset.UTC) == date.withMinute(0).withSecond(0).toEpochSecond(ZoneOffset.UTC)
        }
    }



}