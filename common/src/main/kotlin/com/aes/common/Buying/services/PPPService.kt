package com.aes.common.Buying.services

import org.springframework.stereotype.Service
import java.io.File

@Service
class PPPService {

    val PPPs: Map<Pair<String, Int>, Float> by lazy {
        val file = File(this::class.java.classLoader.getResource("PPP.txt")!!.toURI())
        val lines = file.readLines()
        lines.flatMap {
            val segs = it.split(",")
            val country= segs[0]
            (0..18).map{
                val year = 2010 + it
                val value = segs[it+1].toFloat()
                (country to year) to value
            }
        }.toMap()
    }


    fun getPPPForCountryAtYear(country: String, year: Int): Float{
        return PPPs.getOrDefault(country to year, 0f)
    }

}