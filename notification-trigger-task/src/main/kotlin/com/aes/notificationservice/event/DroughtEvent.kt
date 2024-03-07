package com.aes.notificationservice.event

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Weather.provider.WeatherService
import com.aes.notificationservice.location.Location
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DroughtEvent(
    val weatherService: WeatherService
): NotificationTriggerEvent {

    val droughtThreshold = 0.2

    override val delayDays: Int = 0
    override fun notificationBody(crops: List<Crop>, topics: List<Topic>): String {
        return "A drought is expected in the next couple of days"
    }

    override val executionType: ExecutionType = ExecutionType.CROP_FLAG_DRIVEN { it.shouldWorryAboutDrought }
    override fun shouldTriggerNotification(time: LocalDateTime, location: Location): Boolean {
        val todayWeathers = (0..5).map {
            weatherService.getWeatherForDateAtLocation(location.cityName, location.countryName, time.plusDays(it.toLong()))
        }
        val lastYearWeathers = (0..5).map {
            weatherService.getWeatherForDateAtLocation(location.cityName, location.countryName, time.minusYears(1).plusDays(it.toLong()))
        }

        val sumRailfallToday = todayWeathers.sumOf { (it.precipitation?:0f).toDouble() }
        val sumRailfallLastYear = lastYearWeathers.sumOf { (it.precipitation?:0f).toDouble() }
        return sumRailfallToday < sumRailfallLastYear*droughtThreshold
    }
}