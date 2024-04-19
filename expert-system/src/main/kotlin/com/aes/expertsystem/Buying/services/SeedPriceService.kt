package com.aes.expertsystem.Buying.services

import com.aes.expertsystem.Selling.services.CropSellingService
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDate

/**
 * Extract prices of crops from UK crop price document and store in DB for ease of querying
 * */
@Service
class SeedPriceService(
    val pppService: PPPService,
    val cropSellingService: CropSellingService
) {
    inner class SeedData(
        val prices: Map<Pair<PriceUnit, Float>, Float>,
        val variantName: String
    )

    enum class RowType {
        HEADER,
        BLANK,
        REUNIT,
        CROP
    }

    enum class PriceUnit {
        PKT,
        NUMBER,
        G,
        KG;

        companion object {
            fun fromString(string: String): Pair<PriceUnit, Float> {
                return if (string == "PKT") PKT to 1f
                else if (string.endsWith("kg", true)) KG to string.substring(0, string.length - 2).toFloat()
                else if (string.endsWith("g", true)) G to string.substring(0, string.length - 1).toFloat()
                else if (string.endsWith("s", true)) NUMBER to string.substring(0, string.length - 1).toFloat()
                else throw Exception("Invalid units - $string")
            }
        }
    }

    val seedPrices: Map<String, List<SeedData>> by lazy {
        parseFile(File(this::class.java.classLoader.getResource("SeedPrices.txt")!!.toURI()))
    }

    fun parseHeaderRow(line: String): Pair<String, List<String>> {
        val parts = line.split(",").filter(String::isNotBlank)
        val familyName = parts[0]
        val units = parts.subList(1, parts.size)
        return familyName to units
    }

    fun parseReunitRow(line: String): List<String> {
        return line.split(",").filter(String::isNotBlank)
    }

    fun parseCropRow(line: String, currentHeaderRow: Pair<String, List<Pair<PriceUnit, Float>>>): SeedData {
        val parts = line.split(",").filter(String::isNotBlank)
        val name = parts[1]
        val costs = currentHeaderRow.second.mapIndexedNotNull { index, s ->
            val cost = parts.getOrNull(index + 2)?.toFloatOrNull() ?: return@mapIndexedNotNull null
            s to cost
        }
        return SeedData(costs.toMap(), name)
    }

    fun getRowType(line: String): RowType {
        val parts = line.split(",").filter(String::isNotBlank)
        return if (parts.isEmpty()) RowType.BLANK
        else if (parts.first() == "PKT") RowType.REUNIT
        else if (parts.any { it == "PKT" }) RowType.HEADER
        else RowType.CROP
    }

    fun parseFile(file: File): Map<String, List<SeedData>> {
        val cropList = mutableMapOf<String, MutableList<SeedData>>()
        var currentFamilyName = ""
        var currentUnits = listOf<Pair<PriceUnit, Float>>()

        file.forEachLine {
            try {
                val rowType = getRowType(it)
                when (rowType) {
                    RowType.BLANK -> return@forEachLine
                    RowType.HEADER -> {
                        val (familyName, units) = parseHeaderRow(it)
                        currentFamilyName = familyName
                        currentUnits = units.map(PriceUnit::fromString)
                    }

                    RowType.REUNIT -> {
                        currentUnits = parseReunitRow(it).map(PriceUnit::fromString)
                    }

                    RowType.CROP -> {
                        val seedData = parseCropRow(it, currentFamilyName to currentUnits)
                        if (!cropList.containsKey(currentFamilyName)) {
                            cropList[currentFamilyName] = mutableListOf(seedData)
                        } else {
                            cropList[currentFamilyName]?.add(seedData)
                        }
                    }
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        return cropList
    }

    fun getSeedPriceForCrop(cropName: String): List<SeedData>? {
        return seedPrices[cropName.uppercase()]
            ?: seedPrices.toList()
                .filter { it.first.contains(cropName, true) }
                .flatMap { it.second }
                .ifEmpty { null }

    }

    fun getSeedPriceForCropInCountryOnDate(crop: String, country: String, date: LocalDate): Pair<Float, PriceUnit> {
        val pppUK = pppService.getPPPForCountryAtYear("United Kingdom", date.year)
        val pppCountry = pppService.getPPPForCountryAtYear(country, date.year)
        val pppProp = (pppCountry / pppUK)
        val (cropPrice, priceUnit) = getSeedPriceForCrop(crop)?.maxBulk()
            ?: (cropSellingService.getExpectedPriceForDateInCountry(crop, date, country) / 1000f to PriceUnit.KG)
        val standardBulkDecrease = 0.8f
        return cropPrice * standardBulkDecrease * pppProp to priceUnit
    }

    fun SeedData.convertPrices(): SeedData {
        val newPrices = this.prices.map { (unit, value) ->
            if (unit.first == PriceUnit.G) {
                (PriceUnit.KG to unit.second / 1000f) to value
            } else {
                unit to value
            }
        }.toMap()
        return SeedData(newPrices, this.variantName)
    }

    fun SeedData.maxBulk(): Pair<Pair<PriceUnit, Float>, Float>? {
        val newSeedData = this.convertPrices()
        if (newSeedData.prices.isEmpty()) return null
        val maxUnit = newSeedData.prices.maxBy {
            it.key.first.ordinal
        }.key.first
        val maxValue = newSeedData.prices.filter { it.key.first == maxUnit }.maxBy {
            it.key.second
        }
        return maxValue.toPair()
    }

    fun List<SeedData>.maxBulk(): Pair<Float, PriceUnit> {
        val maxBulks = this.mapNotNull { it.maxBulk() }
        val bestUnit = maxBulks.maxBy {
            it.first.first.ordinal
        }.first.first
        val bestUnits = maxBulks.filter { it.first.first == bestUnit }
        val standardisedByUnit = bestUnits.map {
            it.second / it.first.second
        }
        val average = standardisedByUnit.sum() / standardisedByUnit.size
        return average to bestUnit
    }
}