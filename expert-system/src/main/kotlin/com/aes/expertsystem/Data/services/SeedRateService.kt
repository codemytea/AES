package com.aes.expertsystem.Data.services

import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.CropGroup
import com.aes.common.Weather.provider.WeatherService
import com.aes.expertsystem.Data.Buying.services.SeedDataService
import com.aes.expertsystem.Data.Trefle.TrefleService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Uses standard seed rate formula to calculate the required seedrate given a users smallholding and when they are planning
 * to sow (temperature makes a difference to germination rates)
 * */
@Service
open class SeedRateService(
    val trefleService: TrefleService,
    val seedDataService: SeedDataService,
    val weatherService: WeatherService

) {
    fun String.toTemp(): Float {
        val firstNonDigit = indexOfFirst { !it.isDigit() }
        return substring(0, firstNonDigit).toFloat()
    }

    @Transactional
    fun seedRateForUserSmallholding(
        userSmallholding: UserSmallholding,
        requiredPopn: Int,
        sowingDate: LocalDateTime
    ): Float {
        val crop = userSmallholding.cashCrop ?: return 0f
        val plantInfo = seedDataService.getSeedDataForCrop(crop.name)
        val temperature = weatherService.getWeatherForDateAtLocation(
            userSmallholding.location_city!!,
            userSmallholding.location_country!!,
            sowingDate
        ).temperature!!
        val germinationRate = plantInfo?.germination?.minByOrNull { g ->
            g.temperature?.toTemp()?.let { temperature - it } ?: Float.MAX_VALUE
        }?.temperature?.toTemp()?.let { closestTemp ->
            val availableGerminations = plantInfo.germination.filter { it.temperature?.toTemp() == closestTemp }
            availableGerminations.sumOf { it.temperature!!.toTemp().toDouble() } / (availableGerminations.size * 100)
        } ?: 1.0

        return crop.cropGroup.plantingType.seedRate(
            requiredPopn,
            (plantInfo?.seed_weights?.firstNotNullOfOrNull { it.thousandseedweight } ?: 1f) / 1000,
            1f,
            germinationRate.toFloat(),
        ) * (userSmallholding.smallholdingSize ?: 1.0f)
    }

    @Transactional
    fun seedRateForCropAndSmallholding(
        crop: String,
        cropGroup: CropGroup,
        userSmallholding:UserSmallholding,
        requiredPopn: Int,
        sowingDate: LocalDateTime
    ): Float {
        val plantInfo = seedDataService.getSeedDataForCrop(crop)
        val temperature = weatherService.getWeatherForDateAtLocation(
            userSmallholding.location_city!!,
            userSmallholding.location_country!!,
            sowingDate
        ).temperature!!
        val germinationRate = plantInfo?.germination?.minByOrNull { g ->
            g.temperature?.toTemp()?.let { temperature - it } ?: Float.MAX_VALUE
        }?.temperature?.toTemp()?.let { closestTemp ->
            val availableGerminations = plantInfo.germination.filter { it.temperature?.toTemp() == closestTemp }
            availableGerminations.sumOf { it.temperature!!.toTemp().toDouble() } / (availableGerminations.size * 100)
        } ?: 1.0

        return cropGroup.plantingType.seedRate(
            requiredPopn,
            (plantInfo?.seed_weights?.firstNotNullOfOrNull { it.thousandseedweight } ?: 1f) / 1000,
            1f,
            germinationRate.toFloat(),
        )
    }
}