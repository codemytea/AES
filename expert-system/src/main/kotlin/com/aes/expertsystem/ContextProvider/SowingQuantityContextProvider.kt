package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.services.SeedRateService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.roundToInt

@Component
class SowingQuantityContextProvider(
    private val seedRateService: SeedRateService,
    private val dataCacheService: DataCacheService,
) : ContextProvider, Logging {
    override fun contextForMessage(
        message: String,
        user: User,
    ): List<String> {
        val referencedCrops =
            dataCacheService.cropGroupMap.filter {
                message.contains(it.key, ignoreCase = true)
            }.toList()

        logger().info("Referenced crops [${referencedCrops.joinToString(" && "){it.first}}]")
        return referencedCrops.flatMap { crop ->

            user.userSmallholdingInfo.map {
                val rate =
                    seedRateService.seedRateForCropAndSmallholding(
                        crop.first,
                        crop.second.getCropGroup(),
                        it,
                        100,
                        LocalDateTime.now(),
                    ).roundToDP(3)

                "$rate kilograms of seeds are required to create 100 plants of the crop ${crop.first} on the user's smallholding in ${it.location_country} and city ${it.location_city}"
            }
        } + listOf("The units of seed rate are kilograms per 100 plants") // Really need to hammer home that the unit is kilograms/100 plants here
    }

    fun Float.roundToDP(dp: Int): Float {
        val multiplicator = 10f.pow(dp)
        return (this * multiplicator).roundToInt().toFloat() / multiplicator
    }
}
