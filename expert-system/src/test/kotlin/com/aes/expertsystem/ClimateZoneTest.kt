package com.aes.expertsystem

import com.aes.expertsystem.Data.ClimateZones.model.KoppenClimateZone
import com.aes.expertsystem.Data.ClimateZones.services.ClimateZoneService
import org.junit.jupiter.api.Test

class ClimateZoneTest {
    @Test
    fun climateZonesWork() {
        val serviceUnderTest = ClimateZoneService()
        val result = serviceUnderTest.getClimateZoneForLocation(40.853966f, 14.176562f)
        assert(result == KoppenClimateZone.Csa)
    }
}
