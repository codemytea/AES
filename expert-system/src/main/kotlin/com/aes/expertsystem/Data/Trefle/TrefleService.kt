package com.aes.expertsystem.Data.Trefle

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Data.Trefle.Models.Id
import com.aes.expertsystem.Data.Trefle.Models.PlantListDTO
import com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO.PlantIdResponseDTO
import com.aes.expertsystem.Data.Trefle.Models.Responses.PlantListResponse.PlantListResponseDTO
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@EnableConfigurationProperties(TrefleConfiguration::class)
class TrefleService(
    val trefleConfiguration: TrefleConfiguration,
) : Logging {
    private final inline fun <reified T> RestTemplate.getForEntity(
        url: String,
        params: Map<String, Any>,
    ): T? {
        val realUrl = "$url?${params.toList().joinToString("&") { "${it.first}=${it.second}" }}"
        logger().info(realUrl)
        return getForEntity(realUrl, T::class.java).body
    }

    private val uri = "https://trefle.io/api/v1/"

    /**
     * Get a list of all plants supported by Trefle API
     * */
    fun allPlants(resource: PlantListDTO): PlantListResponseDTO {
        val params =
            resource.toQueryParams().toMutableMap().apply {
                put("token", trefleConfiguration.apiKey)
            }
        return RestTemplateBuilder().build().getForEntity("${uri}plants", params)!!
    }

    /**
     * Get a plant by ID Trefle API
     *
     * @return a response that contains the growing specifications needed
     * */
    fun getPlantById(resource: Id): PlantIdResponseDTO {
        return RestTemplateBuilder().build()
            .getForEntity("${uri}plants/${resource.id}", mapOf<String, Any>(Pair("token", trefleConfiguration.apiKey)))!!
    }

    /**
     * Get a plant by a search query Trefle API
     * */
    fun getPlantBySearchQuery(resource: PlantListDTO): PlantListResponseDTO {
        val params =
            resource.toQueryParams().toMutableMap().apply {
                put("token", trefleConfiguration.apiKey)
            }
        return RestTemplateBuilder().build().getForEntity("${uri}plants/search", params)!!
    }

    /**
     * Get a plant by it's common name eg corn instead of zea Trefle API
     * */
    fun getPlantByCommonName(name: String): PlantListResponseDTO {
        return getPlantBySearchQuery(
            PlantListDTO(
                q = name,
            ),
        )
    }
}
