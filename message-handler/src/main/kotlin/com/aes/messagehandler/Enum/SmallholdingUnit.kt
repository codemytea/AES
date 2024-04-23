package com.aes.messagehandler.Enum

enum class SmallholdingUnit(val convertToHectaresFactor : Float, val unitIdentifier : List<String>) {
    ACRES(0.404686f, listOf("acre", "acres", "a")),
    HECTARES(1f, listOf("hectare", "hectares", "ha")),
    METERS(1E-4f, listOf("meter", "meters", "m", "ms")),
    YARDS(8.3613E-5f, listOf("yard", "yards", "yrd", "yrds")),
    MILE(258.999f, listOf("mile", "miles")),
    KM(100f, listOf("kilometer, kilometers, km"));

    fun getHectares(quantity : Float): Float{
        return quantity*convertToHectaresFactor
    }
}