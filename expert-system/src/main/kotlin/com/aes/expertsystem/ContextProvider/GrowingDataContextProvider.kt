package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.Ecocrop.Services.EcocropDataService
import com.aes.expertsystem.Data.Ecocrop.entities.averageOptimalTemperature
import com.aes.expertsystem.Data.Selling.services.CropSellingService
import org.springframework.stereotype.Component

@Component
class GrowingDataContextProvider(
    private val sellingService: CropSellingService,
    private val dataCacheService: DataCacheService,
    private val ecocropDataService: EcocropDataService,
) : ContextProvider, Logging {
    override fun contextForMessage(
        message: String,
        user: User,
    ): List<String> {
        val referencedCrops =
            dataCacheService.possibleSellingCropNames.filter {
                it.value.any { message.containsAnyCardinality(it, ignoreCase = true) }
            }.map { it.key }

        logger().info("Referenced crops [${referencedCrops.joinToString(" && ")}]")
        return referencedCrops.flatMap {
            val ecocrop = ecocropDataService.getEcocropDataForCrop(it)

            listOf(
                "The minimum crop cycle of $it is ${ecocrop?.minCropCycle ?: "Unknown"} days",
                "The maximum crop cycle of $it is ${ecocrop?.maxCropCycle ?: "Unknown"} days",
                // Doesn't seem to correlate temperature with the weather Data
                "Do not plant $it if the temperature is below ${ecocrop?.absoluteMinTempRequired} degrees celsius on any date.",
                "Do not plant $it if the temperature is above ${ecocrop?.absoluteMaxTempRequired} degrees celsius on any date.",
                "$it will grow fastest if the temperature is ${ecocrop?.averageOptimalTemperature} degrees celsius on any date",
            )
        }
    }
}
