package com.aes.common.Enums

enum class PlantingType {
    DIBBLE,
    BROADCAST;


    fun seedRate(landArea: Float, requiredPlantPopn: Int, seedWeight: Float, purity: Float, germinationRate: Float): Float{
        val fieldFactor = 4
        val methodFactor = 1
        return ((requiredPlantPopn/landArea) * seedWeight * methodFactor)/(purity*germinationRate*fieldFactor)
    }
}