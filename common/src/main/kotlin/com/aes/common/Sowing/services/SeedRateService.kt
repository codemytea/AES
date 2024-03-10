package com.aes.common.Sowing.services

import com.aes.common.Buying.repository.SIDSeedDataFullRepository
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Trefle.TrefleService
import com.aes.common.Weather.provider.WeatherService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
open class SeedRateService(
    val trefleService: TrefleService,
    val sidSeedDataFullRepository: SIDSeedDataFullRepository,
    val weatherService: WeatherService

) {

    fun String.toTemp(): Float{
        val firstNonDigit = indexOfFirst { !it.isDigit() }
        return substring(0, firstNonDigit).toFloat()
    }

    @Transactional
    open fun seedRateForUserSmallholding(userSmallholding: UserSmallholding, requiredPopn: Int, sowingDate: LocalDateTime): Float{
        val crop = userSmallholding.cashCrop ?: return 0f
        val cropInfo = trefleService.getPlantByCommonName(crop.name)
        val (genus, epithet) = cropInfo.data.first().scientific_name.split(" ")
        val plantInfo = sidSeedDataFullRepository.findFirstByGenusAndEpithet(genus, epithet)
        val temperature = weatherService.getWeatherForDateAtLocation(userSmallholding.location_city!!, userSmallholding.location_country!!, sowingDate).temperature!!
        val germinationRate = plantInfo?.germination?.minByOrNull {g-> g.temperature?.toTemp()?.let { temperature-it }?: Float.MAX_VALUE }?.temperature?.toTemp()?.let {closestTemp->
            val availableGerminations = plantInfo.germination.filter { it.temperature?.toTemp() == closestTemp }
            availableGerminations.sumOf { it.temperature!!.toDouble() }/(availableGerminations.size*100)
        } ?: 1.0

        return crop.cropGroup.plantingType.seedRate(
            userSmallholding.smallholdingSize ?: 1f,
            requiredPopn,
            (plantInfo?.seed_weights?.firstOrNull { it.thousandseedweight != null }?.thousandseedweight ?: 1f)/1000,
            1f,
            germinationRate.toFloat(),
        )
    }

}