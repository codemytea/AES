package com.aes.common

import com.aes.common.Buying.services.PPPService
import com.aes.common.Buying.services.SeedPriceService
import org.junit.jupiter.api.Test

class SeedPriceTests {


    @Test
    fun gettingSeedPriceForCountryWorks(){
        val seedPriceService = SeedPriceService(PPPService())
        val price = seedPriceService.getSeedPriceForCropInCountryInYear("Tomato", "Australia", 2024)
        println("Price is ${price.first} per ${price.second.name}")
    }
}