package com.aes.expertsystem.Data.Selling.services

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.Selling.entities.CropPrice
import com.aes.expertsystem.Data.Selling.repository.CropPriceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
open class CropPriceSavingService(
    val cropPriceRepository: CropPriceRepository
) : Logging {

    @Transactional
    open fun writeAllToDB() {
        writeDataToDatabase(
            File(
                this::class.java.classLoader.getResource("FAOSTATSellingDataCompressed.txt")!!.toURI()
            )
        )
    }

    fun writeDataToDatabase(inputFile: File) {
        var count = 1
        inputFile.forEachLine {
            if (count++ % 50000 == 0) {
                logger().info("Processed $count")
            }
            cropPriceRepository.save(CropPrice.fromFileLine(it))
        }
    }
}