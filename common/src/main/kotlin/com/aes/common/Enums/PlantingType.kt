package com.aes.common.Enums

/**
 * Used for seed rate calculations - you need less seed if you dibble as it's more precise, and more if you
 * broadcast.
 * */
enum class PlantingType {
    DIBBLE,
    BROADCAST;

    fun seedRate(requiredPlantPopn: Int, seedWeight: Float, purity: Float, germinationRate: Float): Float{
        val fieldFactor = 4
        val methodFactor = 3
        return ((requiredPlantPopn) * seedWeight * methodFactor)/(purity*germinationRate*fieldFactor)
    }
}