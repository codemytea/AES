package com.aes.common.Weather.geocoding

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class GeocodingService {
    private fun url(name: String) = "https://geocoding-api.open-meteo.com/v1/search?name=$name&count=1&language=en&format=json"
    fun getLatLngForName(locationName:String): LatLng{
        return RestTemplateBuilder().build().getForEntity(url(locationName), GeocodingResults::class.java).body!!.results.first()
    }
}