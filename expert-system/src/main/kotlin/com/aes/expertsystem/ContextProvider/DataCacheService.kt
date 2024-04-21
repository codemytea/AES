package com.aes.expertsystem.ContextProvider

import com.aes.expertsystem.Data.Buying.services.SeedPriceService
import com.aes.expertsystem.Data.Selling.repository.CropPriceRepository
import org.springframework.stereotype.Service

@Service
class DataCacheService(
    private val cropPriceRepository: CropPriceRepository,
    private val seedPriceService: SeedPriceService
) {

    private fun String.clean(): List<String>{
        return listOf(
            this,
            *this.split(" ")
                .map {
                    it.removePrefix("(")
                        .removeSuffix(",")
                        .removeSuffix(")")
                }
                .filter {
                    it.lowercase() != "and"
                        && it.lowercase() != "meat"
                        && it.lowercase() != "of"
                        && it.lowercase() != "or"
                        && it.lowercase() != "n.e.c."
                        && it.lowercase() != "spp."
                        && it.lowercase() != "other"
                        && it.lowercase() != "with"
                        && it.lowercase() != "in"
                        && it.lowercase() != "the"
                        && it.lowercase() != "for"
                        && it.lowercase() != "reeling"
                        && it.lowercase() != "fresh"
                        && it.lowercase() != "high"
                }.toTypedArray()
        )
    }

    val possibleSellingCropNames by lazy {
        cropPriceRepository.findCropNames().associateWith {
            it.clean()
        }
    }

    val possibleSeedBuyingCropNames by lazy {
        seedPriceService.seedPrices.flatMap {
            it.key.clean()
        }
    }
}