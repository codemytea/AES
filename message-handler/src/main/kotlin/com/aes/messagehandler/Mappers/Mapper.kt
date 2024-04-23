package com.aes.messagehandler.Mappers

import com.aes.common.Entities.Message
import com.aes.common.Enums.Crop
import com.aes.common.Enums.MessageStatus
import com.aes.common.Enums.UserDetails
import com.aes.common.Models.MessageDTO
import com.aes.messagehandler.Enum.SmallholdingUnit

fun String.toUserDetails(): UserDetails? {

    if (this == "locationCity") return UserDetails.LOCATION_CITY
    if (this == "locationCountry") return UserDetails.LOCATION_COUNTRY
    if (this == "mainCrop") return UserDetails.MAIN_CROP
    if (this == "smallholdingSize") return UserDetails.SMALLHOLDING_SIZE
    if (this == "name") return UserDetails.NAME
    return null

}


fun String.toCrop(): Crop? {
    if (this == "MAIZE") return Crop.MAIZE
    if (this == "WHEAT") return Crop.WHEAT
    if (this == "SOY_BEANS") return Crop.SOY_BEANS
    if (this == "RICE") return Crop.RICE
    if (this == "BARLEY") return Crop.BARLEY
    return null
}

fun Message.toDTO(): MessageDTO {
    return MessageDTO(
        id,
        user.id,
        message,
        phoneNumber,
        status ?: MessageStatus.PENDING
    )
}


fun String.toHectares(): Float? {
    return when {
        containsIgnoringPlurality("acre")-> this.removeAndConvert(SmallholdingUnit.ACRES)
        containsIgnoringPlurality("hectare")-> this.removeAndConvert(SmallholdingUnit.HECTARES)
        containsIgnoringPlurality("meter")->3f
        containsIgnoringPlurality("yard")->3f
        else ->  null
    }
}

fun String.containsIgnoringPlurality(str: String): Boolean {
    return (this.contains(str, true) || this.contains(str+"s", true))
}

fun String.removeAndConvert(sUnit : SmallholdingUnit): Float {
    return sUnit.getHectares(this.replaceList(sUnit.unitIdentifier, "").trim().toFloat())
}

fun String.replaceList(toReplace : List<String>, replaceToken: String) : String {
    toReplace.forEach {
        this.replace(it, replaceToken)
    }
    return this
}