package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Weather.model.WeatherInfo
import com.aes.common.Weather.provider.WeatherService
import com.aes.common.logging.Logging
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class WeatherContextProvider(
    private val weatherService: WeatherService,
) : ContextProvider, Logging {
    fun getWeatherData(smallholding: UserSmallholding): List<WeatherInfo> {
        return weatherService.getWeatherBetweenDatesAtLocation(
            smallholding.location_city!!,
            smallholding.location_country!!,
            LocalDate.now(),
            LocalDate.now().plusDays(7),
        )
    }

    override fun contextForMessage(
        message: String,
        user: User,
    ): List<String> {
        return user.userSmallholdingInfo.mapNotNull {
            if (it.location_city == null || it.location_country == null) return@mapNotNull null
            getWeatherData(it).flatMap {
                listOf(
                    "The temperature on date ${it.time.format(DateTimeFormatter.ISO_DATE)} " +
                        "at time ${it.time.format(DateTimeFormatter.ISO_TIME)} is expected to be " +
                        "${it.temperature ?: 0f} degrees celsius",
                    "The rainfall on date ${it.time.format(DateTimeFormatter.ISO_DATE)} " +
                        "at time ${it.time.format(DateTimeFormatter.ISO_TIME)} is expected to be " +
                        "${it.precipitation ?: 0f}mm",
                    "The cloud cover on date ${it.time.format(DateTimeFormatter.ISO_DATE)} " +
                        "at time ${it.time.format(DateTimeFormatter.ISO_TIME)} is expected to be " +
                        "${it.cloudCoverPercentage ?: 0f}%",
                    "The snowfall on date ${it.time.format(DateTimeFormatter.ISO_DATE)} " +
                        "at time ${it.time.format(DateTimeFormatter.ISO_TIME)} is expected to be " +
                        "${it.snowfallCm ?: 0f}cm",
                    "The humidity on date ${it.time.format(DateTimeFormatter.ISO_DATE)} " +
                        "at time ${it.time.format(DateTimeFormatter.ISO_TIME)} is expected to be" +
                        " ${it.humidity ?: 0f}%",
                )
            }
        }.flatten()
    }
}
