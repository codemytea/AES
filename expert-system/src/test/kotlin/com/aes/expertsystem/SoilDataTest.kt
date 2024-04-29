package com.aes.expertsystem

import com.aes.expertsystem.Data.Soil.model.SoilType
import com.aes.expertsystem.Data.Soil.services.SoilTypeFetchService
import org.junit.jupiter.api.Test

class SoilDataTest {
    @Test
    fun soilDataFetchWorks() {
        val serviceUnderTest = SoilTypeFetchService()
        val result = serviceUnderTest.getSoilTypeForLocation(42f, 0f)
        assert(result == SoilType.Cambisols)
    }
}
