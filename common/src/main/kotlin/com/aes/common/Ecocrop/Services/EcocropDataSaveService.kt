package com.aes.common.Ecocrop.Services

import com.aes.common.Ecocrop.entities.EcocropData
import com.aes.common.Ecocrop.repository.EcocropDataRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

/**
 * Service to get Crop Data from ECOCROP (run by FAOSTAT)
 * - no csv/api so iterate through webpages and use JSOUP
 * Information stored in DB
 *
 * This service writes all collected data to the DB
 * */
@Service
open class EcocropDataSaveService(
    val ecocropDataRepository: EcocropDataRepository
) : Logging {

    @Transactional
    open fun writeAllToDB() {
        writeDataToDatabase(File(this::class.java.classLoader.getResource("ecocropData.txt")!!.toURI()))
    }

    fun writeDataToDatabase(inputFile: File) {
        val mapper = jacksonObjectMapper()
        val typeRef = mapper.typeFactory.constructCollectionType(List::class.java, EcocropData::class.java)
        val data = jacksonObjectMapper().readValue<List<EcocropData>>(inputFile, typeRef)
        var count = 1

        data.forEach {
            if (count++ % 500 == 0) {
                logger().info("Processed $count")
            }
            ecocropDataRepository.save(it)
        }
    }
}