package com.aes.expertsystem.Data.Soil.services

import com.aes.expertsystem.Data.Soil.model.SoilAPIResponse
import com.aes.expertsystem.Data.Soil.model.SoilType
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity

@Service
class SoilTypeFetchService {
    private val link = { lat: Float, lng: Float ->
        "https://rest.isric.org/soilgrids/v2.0/classification/query?lon=$lng&lat=$lat&number_classes=0"
    }

    fun getSoilTypeForLocation(
        lat: Float,
        lng: Float,
    ): SoilType {
        return RestTemplateBuilder()
            .build().getForEntity<SoilAPIResponse>(link(lat, lng)).body?.mostProbableSoilType
            ?: throw Exception("Request for soil type failed")
    }
}
