package com.aes.expertsystem.ContextProvider

import com.aes.expertsystem.Data.Buying.services.SeedPriceService
import com.aes.expertsystem.Data.CropGroup.repositories.CropGroupRepository
import com.aes.expertsystem.Data.Selling.repository.CropPriceRepository
import org.springframework.stereotype.Service

@Service
class DataCacheService(
    private val cropPriceRepository: CropPriceRepository,
    private val seedPriceService: SeedPriceService,
    private val cropGroupRepository: CropGroupRepository
) {
    
    val ignoreWords = listOf(
        "and",
        "meat",
        "of",
        "or",
        "n.e.c.",
        "spp.",
        "other",
        "with",
        "in",
        "the",
        "for",
        "reeling",
        "fresh",
        "high",
        "dried",
        "leaves",
        "seeds",
        "seed",
        "other",
        "shell",
        "birds",
        "from",
        "suitable"
    )

    private fun String.clean(): List<String>{
        return listOf(
            this,
            *this.split(" ")
                .map {
                    it.removePrefix("(")
                        .removeSuffix(",")
                        .removeSuffix(")")
                }
                .filter {!ignoreWords.contains(it.lowercase())}
                .toTypedArray()
        )
    }

    val possibleSellingCropNames by lazy {
        cropPriceRepository.findCropNames().associateWith {
            it.clean()
        }
    }

    fun <T> List<T>.subList(fromIndex: Int): List<T>{
        return this.subList(fromIndex, this.size)
    }

    val cropGroupMap by lazy {
        cropGroupRepository.findAll().filter { it.subgroupLetter.isBlank() }.flatMap { cge->
            cge.entries.map { it.name to cge }
        }.toMap()
    }

    val possibleSeedBuyingCropNames by lazy {
        seedPriceService.seedPrices.map {
            it.key to it.key.clean()
        }.toMap()
    }
}