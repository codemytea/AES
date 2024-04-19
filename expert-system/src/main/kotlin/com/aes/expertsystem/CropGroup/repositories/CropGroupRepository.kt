package com.aes.expertsystem.CropGroup.repositories

import com.aes.expertsystem.CropGroup.entities.CropGroupEntity
import com.aes.expertsystem.CropGroup.entities.CropGroupId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropGroupRepository : CrudRepository<CropGroupEntity, CropGroupId>