package com.aes.expertsystem

import com.aes.expertsystem.Data.Buying.entities.SIDSeedDataFull
import com.aes.expertsystem.Buying.services.RawRequestService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.io.File

class SeedWeightTests {


    @Test
    fun getSeedWeightRequestWorks() {
        val lettersFile = File(this::class.java.classLoader.getResource("letters.txt")!!.toURI())
        val letters = lettersFile.readLines()
        var complete = 0
        val singleThreadDispatcher = CoroutineScope(Dispatchers.Default.limitedParallelism(1))
        val results = runBlocking(Dispatchers.IO) {
            println("START OF REQUESTS")
            letters.map { letter ->
                async {
                    var result: List<RawRequestService.SIDSeedData>? = null
                    for (i in 0..10) {
                        result = RawRequestService.sendGETForIds(letter)
                        if (result != null) break
                    }
                    result.also {
                        complete++
                        println("Completed $complete out of ${letters.size} ($letter) ${if (it == null) "FAIL" else if (it.size > 999) "OVERSIZE" else "SUCCESS ${it.size}"}")
                    }
                }
            }.awaitAll()
        }.mapNotNull { it }.flatten()
        val resultsFile = File(lettersFile.absolutePath.removeSuffix("letters.txt").plus("results.txt"))
        jacksonObjectMapper().writeValue(resultsFile, results)
    }

    @Test
    fun getSeedWeightFullRequestWorks() {
        val skeletonsFile = File(this::class.java.classLoader.getResource("skeletons.txt")!!.toURI())
        val mapper = jacksonObjectMapper()
        val typeRef =
            mapper.typeFactory.constructCollectionType(List::class.java, RawRequestService.SIDSeedData::class.java)
        // print result
        val skeletons = jacksonObjectMapper().readValue<List<RawRequestService.SIDSeedData>>(skeletonsFile, typeRef)
        var complete = 0
        val results = runBlocking(Dispatchers.IO) {
            println("START OF REQUESTS")
            skeletons.map { sk ->
                async {
                    var result: com.aes.expertsystem.Data.Buying.entities.SIDSeedDataFull? = null
                    for (i in 0..10) {
                        result = RawRequestService.setGETForFull(sk)
                        if (result != null) break
                    }
                    result.also {
                        complete++
                        println("Completed $complete out of ${skeletons.size} (${sk.genus}) ${if (it == null) "FAIL" else "SUCCESS"}")
                    }
                }
            }.awaitAll()
        }.mapNotNull { it }
        val resultsFile = File(skeletonsFile.absolutePath.removeSuffix("skeletons.txt").plus("results.txt"))
        jacksonObjectMapper().writeValue(resultsFile, results)
    }
}