package com.aes.expertsystem.Data.Buying.services

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.Buying.entities.SIDSeedDataFull
import com.aes.expertsystem.Data.Buying.repository.SIDSeedDataFullRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

/**
 * Writes Data extracted from SID dataset to database for ease of querying
 * */
@Service
open class SeedDataSavingService(
    val sidSeedDataFullRepository: SIDSeedDataFullRepository
) : Logging {

    @Transactional
    open fun writeAllToDB() {
        writeDataToDatabase(File(this::class.java.classLoader.getResource("seed_data.txt")!!.toURI()))
    }

    fun writeDataToDatabase(inputFile: File) {
        val mapper = jacksonObjectMapper()
        val typeRef = mapper.typeFactory.constructCollectionType(List::class.java, SIDSeedDataFull::class.java)
        val data = jacksonObjectMapper().readValue<List<SIDSeedDataFull>>(inputFile, typeRef)
        var count = 1
        data.forEach {
            if (count++ % 5000 == 0) {
                logger().info("Processed $count")
            }
            sidSeedDataFullRepository.save(it)
        }
    }
}