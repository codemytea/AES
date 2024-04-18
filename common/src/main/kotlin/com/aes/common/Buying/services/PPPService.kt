package com.aes.common.Buying.services

import org.springframework.stereotype.Service
import java.io.File

//NB: PPP = Purchasing Power Parity
@Service
class PPPService {

    /**
     * In memory map for the purchasing power parity (reduces query time and not very big so doesn't need to be stored in DB)
     * Map<Pair<CountryName, TheYearConcerned>, ThePPP>
     *     For all years (about 20)
     * */
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


    /**
     * Function to access the PPP of a given country in a given year
     *
     * @param country the country
     * @param year the year
     * @return the PPP
     * */
    fun getPPPForCountryAtYear(country: String, year: Int): Float{
        return PPPs.getOrDefault(country to year, 0f)
    }

}