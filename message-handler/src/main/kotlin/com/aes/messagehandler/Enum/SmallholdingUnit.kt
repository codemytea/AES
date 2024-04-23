package com.aes.messagehandler.Enum

enum class SmallholdingUnit(val convertToHectaresFactor : Float, val unitIdentifier : List<String>) {
    ACRES(1.1f, listOf("acre", "acres")),
    HECTARES(1f, listOf("hectare", "hectares"));

    fun getHectares(quantity : Float): Float{
        return quantity*convertToHectaresFactor
    }
}