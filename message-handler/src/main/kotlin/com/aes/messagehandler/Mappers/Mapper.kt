package com.aes.messagehandler.Mappers

import com.aes.common.Entities.Message
import com.aes.common.Enums.Crop
import com.aes.common.Enums.MessageStatus
import com.aes.common.Enums.UserDetails
import com.aes.common.Models.MessageDTO

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
