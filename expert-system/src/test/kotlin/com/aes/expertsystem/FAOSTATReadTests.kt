package com.aes.expertsystem

import com.aes.expertsystem.Data.Selling.services.CropPriceParsingService
import org.junit.jupiter.api.Test
import java.io.File

class FAOSTATReadTests {
    @Test
    fun canReadFAOSTATData() {
        val cropPriceParsingService = CropPriceParsingService()
        val fromFileName = "FAOSTATSellingData.csv"
        val fromFile = File(this::class.java.classLoader.getResource(fromFileName)!!.toURI())
        val toFile = File(fromFile.absolutePath.removeSuffix(fromFileName).plus("FAOSTATSellingDataCompressed.txt"))
        cropPriceParsingService.compressData(
            fromFile, toFile
        )
    }

    @Test
    fun canReadCompressedFAOSTATData() {
        val cropPriceParsingService = CropPriceParsingService()
        val fromFileName = "FAOSTATSellingData.csv"
        val fromFile = File(this::class.java.classLoader.getResource(fromFileName)!!.toURI())
        val toFile = File(fromFile.absolutePath.removeSuffix(fromFileName).plus("FAOSTATSellingDataCompressed.txt"))
        cropPriceParsingService.compressData(
            fromFile, toFile
        )
    }
}