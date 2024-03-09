package com.aes.common.Buying.services

import com.aes.common.Buying.entities.SIDSeedDataFull
import com.aes.common.Buying.repository.SIDSeedDataFullRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
open class SeedDataSavingService(
    val sidSeedDataFullRepository: SIDSeedDataFullRepository
): Logging {

    @Transactional
    open fun writeAllToDB(){
        writeDataToDatabase(File(this::class.java.classLoader.getResource("seed_data.txt")!!.toURI()))
    }
    fun writeDataToDatabase(inputFile: File) {
        val mapper = jacksonObjectMapper()
        val typeRef = mapper.typeFactory.constructCollectionType(List::class.java, SIDSeedDataFull::class.java)
        // print result
        val data = jacksonObjectMapper().readValue<List<SIDSeedDataFull>>(inputFile, typeRef)
        var count = 1
        data.forEach {
            if(count++ % 5000 == 0){
                logger().info("Processed $count")
            }
            sidSeedDataFullRepository.save(it)
        }
    }
}