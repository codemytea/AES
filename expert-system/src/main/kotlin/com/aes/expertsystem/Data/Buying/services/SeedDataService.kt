package com.aes.expertsystem.Data.Buying.services

import com.aes.expertsystem.Data.Buying.entities.SIDSeedDataFull
import com.aes.expertsystem.Data.Buying.repository.SIDSeedDataFullRepository
import com.aes.expertsystem.Data.Trefle.TrefleService
import org.springframework.stereotype.Service

@Service
class SeedDataService(
    val sidSeedDataFullRepository: SIDSeedDataFullRepository,
    val trefleService: TrefleService
) {

    fun getSeedDataForCrop(cropName: String): SIDSeedDataFull?{
        val cropInfo = trefleService.getPlantByCommonName(cropName)
        val (genus, epithet) = cropInfo.data.first().scientific_name.split(" ")
        return sidSeedDataFullRepository.findFirstByGenusAndEpithet(genus, epithet)
    }

}