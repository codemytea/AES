package com.aes.expertsystem.Data.ClimateZones.services

import com.aes.expertsystem.Data.ClimateZones.model.ClimateZoneAPIResponse
import com.aes.expertsystem.Data.ClimateZones.model.KoppenClimateZone
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity

@Service
class ClimateZoneService {

    private val link = {lat: Float, lng: Float ->
        "http://climateapi.scottpinkelman.com/api/v1/location/$lat/$lng"
    }

    fun getClimateZoneForLocation(lat: Float, lng: Float): KoppenClimateZone{
        return RestTemplateBuilder().build()
            .getForEntity<ClimateZoneAPIResponse>(link(lat, lng)).body
            ?.returnValues?.firstOrNull()?.koppenClimateZone
            ?: throw Exception("Climate zone API request failed")
    }
}