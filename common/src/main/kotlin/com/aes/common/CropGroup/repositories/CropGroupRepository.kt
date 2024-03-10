package com.aes.common.CropGroup.repositories

import com.aes.common.CropGroup.entities.CropGroupEntity
import com.aes.common.CropGroup.entities.CropGroupId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropGroupRepository: CrudRepository<CropGroupEntity, CropGroupId>