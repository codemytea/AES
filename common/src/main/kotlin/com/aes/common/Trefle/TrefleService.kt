package com.aes.common.Trefle

import com.aes.common.Trefle.Models.Id
import com.aes.common.Trefle.Models.PlantListDTO
import com.aes.common.Trefle.Models.Responses.PlantIdResponseDTO.PlantIdResponseDTO
import com.aes.common.Trefle.Models.Responses.PlantListResponse.PlantListResponseDTO
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class TrefleService: Logging {

    private val key = "MtG0fklvA_J60KpvmuCqhKuwKLXsjYqjS5vXuqKLXWA"

    private final inline fun <reified T> RestTemplate.getForEntity(url: String, params: Map<String, Any>): T? {
        val realUrl = "$url?${params.toList().joinToString("&") { "${it.first}=${it.second}" }}"
        logger().info(realUrl)
        return getForEntity(realUrl, T::class.java).body
    }

    private val uri = "https://trefle.io/api/v1/"

    fun allPlants(resource: PlantListDTO): PlantListResponseDTO {
        val params = resource.toQueryParams().toMutableMap().apply {
            put("token", key)
        }
        return RestTemplateBuilder().build().getForEntity("${uri}plants", params)!!
    }

    fun getPlantById(resource: Id): PlantIdResponseDTO {
        return RestTemplateBuilder().build()
            .getForEntity("${uri}plants/${resource.id}", mapOf<String, Any>(Pair("token", key)))!!
    }


    fun getPlantBySearchQuery(resource: PlantListDTO): PlantListResponseDTO {
        val params = resource.toQueryParams().toMutableMap().apply {
            put("token", key)
        }
        return RestTemplateBuilder().build().getForEntity("${uri}plants/search", params)!!
    }

    fun getPlantByCommonName(name: String): PlantListResponseDTO {
        return getPlantBySearchQuery(PlantListDTO(
            q=name
        ))
    }


}




