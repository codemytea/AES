package com.aes.common.CropGroup.services

import com.aes.common.CropGroup.entities.CropGroupEntity
import com.aes.common.CropGroup.repositories.CropGroupEntryRepository
import com.aes.common.CropGroup.repositories.CropGroupRepository
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