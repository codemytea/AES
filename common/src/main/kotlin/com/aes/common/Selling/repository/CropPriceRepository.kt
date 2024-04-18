package com.aes.common.Selling.repository

import com.aes.common.Selling.entities.CropPrice
import com.aes.common.Selling.entities.CropPriceId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropPriceRepository : CrudRepository<CropPrice, CropPriceId> {
    fun findAllByCropNameAndCountryLike(cropName: String, country: String): List<CropPrice>
}