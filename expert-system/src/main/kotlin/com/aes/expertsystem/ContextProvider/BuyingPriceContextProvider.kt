package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import com.aes.expertsystem.Data.Buying.services.SeedPriceService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class BuyingPriceContextProvider(
    private val seedPriceService: SeedPriceService,
    private val dataCacheService: DataCacheService
): ContextProvider {

    override fun contextForMessage(message: String, user: User): List<String> {
        val referencedCrops = dataCacheService.possibleSellingCropNames.filter {
            it.value.any{message.contains(" $it ", ignoreCase = true)}
        }.map { it.key }
        val country = user.userSmallholdingInfo.first().location_country!!
        return (1 until 120).flatMap {
            val dateFuture = LocalDate.now().plusDays(it.toLong())
            referencedCrops.map {
                val futurePrice = seedPriceService.getSeedPriceForCropInCountryOnDate(
                    it, country, dateFuture
                )
                "Seeds of $it predicted to cost $futurePrice on ${dateFuture.format(DateTimeFormatter.ISO_DATE)}"
            }
        }

    }
}