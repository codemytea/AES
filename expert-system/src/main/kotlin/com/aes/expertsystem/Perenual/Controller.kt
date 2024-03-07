package com.aes.expertsystem.Perenual

import com.aes.common.logging.Logging
import com.aes.expertsystem.Models.PlantDetailsDTO
import com.aes.expertsystem.Models.PlantDiseasesDTO
import com.aes.expertsystem.Models.PlantListDTO
import com.aes.expertsystem.Models.Responses.PlantDetails.PlantDetailsResponseDTO
import com.aes.expertsystem.Models.Responses.PlantDisease.PlantDiseaseResponseDTO
import com.aes.expertsystem.Models.Responses.PlantList.PlantListResponseDTO
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service


@Service
class Controller(
) : Logging {

    private val key = "PRTt65e740fc544e74470"
    private val uri = "https://perenual.com/api"

    fun getSpecies(resource: PlantListDTO): PlantListResponseDTO {
        return RestTemplateBuilder().build().getForEntity("${uri}/species-list?key=$key&", PlantListResponseDTO::class.java, resource.toQueryParams()).body!!
    }

    fun getDetails( resource: PlantDetailsDTO): PlantDetailsResponseDTO {
        return RestTemplateBuilder().build().getForEntity("${uri}/species/details/${resource.id}?key=$key&", PlantDetailsResponseDTO::class.java).body!!
    }


    fun getDiseases( resource: PlantDiseasesDTO): PlantDiseaseResponseDTO {
        return RestTemplateBuilder().build().getForEntity("${uri}/pest-disease-list?key=$key&", PlantDiseaseResponseDTO::class.java).body!!
    }


}




