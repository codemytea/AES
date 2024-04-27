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
        val referencedCrops = dataCacheService.possibleSeedBuyingCropNames.filter {
            it.value.any{message.containsWord(it, ignoreCase = true)}
        }.map { it.key }
        val country = user.userSmallholdingInfo.firstOrNull()?.location_country ?: "United Kingdom"
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