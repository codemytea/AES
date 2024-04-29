package com.aes.expertsystem.Data.Ecocrop.Services

import com.aes.expertsystem.Data.Ecocrop.entities.EcocropData
import com.aes.expertsystem.Data.Ecocrop.repository.EcocropDataRepository
import com.aes.expertsystem.Data.Trefle.TrefleService
import org.springframework.stereotype.Service

@Service
class EcocropDataService(
    val trefleService: TrefleService,
    val ecocropDataRepository: EcocropDataRepository,
) {
    fun getEcocropDataForCrop(crop: String): EcocropData? {
        val c = trefleService.getPlantByCommonName(crop).data.firstOrNull() ?: return null
        val (genus, epithet) = c.scientific_name.split(" ")
        return ecocropDataRepository.findFirstByScientificName("$genus $epithet") ?: c.synonyms?.firstNotNullOfOrNull {
            ecocropDataRepository.findFirstByScientificName(it)
        }
    }
}
