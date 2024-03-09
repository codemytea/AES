package com.aes.common.Enums

enum class Crop(val shouldWorryAboutDrought: Boolean = false) {
    MAIZE(true),
    WHEAT,
    SOY_BEANS,
    RICE,
    BARLEY
}
