package com.aes.common.Enums

enum class PlantingType {
    DIBBLE,
    BROADCAST;


    fun seedRate(requiredPlantPopn: Int, seedWeight: Float, purity: Float, germinationRate: Float): Float{
        val fieldFactor = 4
        val methodFactor = 3
        return ((requiredPlantPopn) * seedWeight * methodFactor)/(purity*germinationRate*fieldFactor)
    }
}