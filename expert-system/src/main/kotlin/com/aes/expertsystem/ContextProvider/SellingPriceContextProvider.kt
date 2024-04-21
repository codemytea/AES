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
    private val dataCacheService: DataCacheService
): ContextProvider, Logging {

    override fun contextForMessage(message: String, user: User): List<String> {
        val referencedCrops = dataCacheService.possibleSellingCropNames.filter {
            it.value.any{ message.contains(" $it ", ignoreCase = true) }
        }.map { it.key }

        logger().info("Referenced crops [${referencedCrops.joinToString(" && ")}]")
        val country = user.userSmallholdingInfo.first().location_country!!
        return (1 until 60).flatMap {
            val datePast = LocalDate.now().minusDays(it.toLong())
            val dateFuture = LocalDate.now().plusDays(it.toLong())
            referencedCrops.flatMap {
                val pastPrice = sellingService.getExpectedPriceForDateInCountry(
                    it, datePast, country
                )
                val futurePrice = sellingService.getExpectedPriceForDateInCountry(
                    it, dateFuture, country
                )
                listOf(
                    "Data Point Provided: $it sold for $pastPrice on ${datePast.format(DateTimeFormatter.ISO_DATE)}",
                    "Data Point Provided: $it sold for $futurePrice on ${dateFuture.format(DateTimeFormatter.ISO_DATE)}"
                )
            }
        }

    }
}