package com.aes.messagehandler.Enum

enum class SmallholdingUnit(val convertToHectaresFactor: Float, val unitIdentifier: List<String>) {
    ACRES(0.404686f, listOf("acres", "acre", "a")),
    HECTARES(1f, listOf("hectares", "hectare", "ha")),
    METERS(1E-4f, listOf("meters", "meter", "ms", "m")),
    YARDS(8.3613E-5f, listOf("yards", "yard", "yrds", "yrd")),
    MILE(258.999f, listOf("miles", "mile")),
    KM(100f, listOf("kilometers", "kilometer", "kms", "km")),
    ;

    fun getHectares(quantity: Float): Float {
        return quantity * convertToHectaresFactor
    }
}
