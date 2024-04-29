package com.aes.expertsystem.Data.CropGroup.repositories

import com.aes.expertsystem.Data.CropGroup.entities.CropGroupEntity
import com.aes.expertsystem.Data.CropGroup.entities.CropGroupId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropGroupRepository : CrudRepository<CropGroupEntity, CropGroupId>
