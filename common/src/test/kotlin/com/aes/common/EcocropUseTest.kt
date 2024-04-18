package com.aes.common

import com.aes.common.Ecocrop.Services.FullPageParseService
import com.aes.common.Ecocrop.Services.SummaryPageParseService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File

class EcocropUseTest {

    val letters = "abcdefghijklmnopqrstuvwxyz"


    @Test
    fun getAndStoreDataWorks(){
        val summaryService = SummaryPageParseService()
        val lettersSingle = letters.map { it.toString() }
        val ids = lettersSingle.flatMap {
            println("Fetching ids for letter $it")
            summaryService.getIdsForLetter(it)
        }
        println("Found ${ids.size} ids")

        val fullService = FullPageParseService()
        var complete = 0
        val results = runBlocking(Dispatchers.IO) {
            println("START OF REQUESTS")
            ids.map {id->
                async {
                    fullService.getData(id).also{
                        complete++
                        println("Completed $complete out of ${ids.size} ($id)")
                    }
                }
            }.awaitAll()
        }

        val file = File("results.txt")
        jacksonObjectMapper().writeValue(file, results)
        println("Written results to ${file.absolutePath}")

    }

}