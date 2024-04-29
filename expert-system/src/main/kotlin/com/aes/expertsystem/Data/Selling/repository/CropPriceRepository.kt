package com.aes.expertsystem.Data.Selling.repository

import com.aes.expertsystem.Data.Selling.entities.CropPrice
import com.aes.expertsystem.Data.Selling.entities.CropPriceId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CropPriceRepository : CrudRepository<CropPrice, CropPriceId> {
    fun findAllByCropNameAndCountryLike(
        cropName: String,
        country: String,
    ): List<CropPrice>

    @Query("SELECT DISTINCT CP.cropName from CropPrice CP")
    fun findCropNames(): List<String>
}
