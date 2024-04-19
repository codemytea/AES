package com.aes.expertsystem.Data.CropGroup.repositories

import com.aes.expertsystem.Data.CropGroup.entities.CropGroupEntry
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropGroupEntryRepository : CrudRepository<CropGroupEntry, String> {

    fun findAllByNameLikeIgnoreCase(cropName: String): List<CropGroupEntry>

}