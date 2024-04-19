package com.aes.expertsystem.CropGroup.services

import com.aes.expertsystem.CropGroup.entities.CropGroupEntity
import com.aes.expertsystem.CropGroup.repositories.CropGroupEntryRepository
import com.aes.expertsystem.CropGroup.repositories.CropGroupRepository
import org.springframework.stereotype.Service

@Service
class CropGroupFetchService(
    val cropGroupEntryRepository: CropGroupEntryRepository,
    val cropGroupRepository: CropGroupRepository
) {

    fun getAllMainGroupsByName(name: String): List<CropGroupEntity> {
        return cropGroupEntryRepository.findAllByNameLikeIgnoreCase("%$name%").mapNotNull {
            it.cropSubGroup
        }.filter { it.isMain() }.toSet().toList()
    }

    fun getAllSubGroupsByName(name: String): List<CropGroupEntity> {
        return cropGroupEntryRepository.findAllByNameLikeIgnoreCase("%$name%").mapNotNull {
            it.cropSubGroup
        }.filter { !it.isMain() }.toSet().toList()
    }

}