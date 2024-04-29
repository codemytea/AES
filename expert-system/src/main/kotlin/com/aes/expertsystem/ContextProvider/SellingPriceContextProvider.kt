package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.Selling.services.CropSellingService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class SellingPriceContextProvider(
    private val sellingService: CropSellingService,
    private val dataCacheService: DataCacheService,
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
        val country = user.userSmallholdingInfo.firstOrNull()?.location_country ?: "United Kingdom"
        return (1 until 120).flatMap {
            val dateFuture = LocalDate.now().plusDays(it.toLong())
            referencedCrops.map {
                val futurePrice =
                    sellingService.getExpectedPriceForDateInCountry(
                        it,
                        dateFuture,
                        country,
                    )
                "$it predicted to be sold for $futurePrice on date ${dateFuture.format(DateTimeFormatter.ISO_DATE)}"
                "$it cannot be sold until it has finished its crop cycle"
            }
        }
    }
}
