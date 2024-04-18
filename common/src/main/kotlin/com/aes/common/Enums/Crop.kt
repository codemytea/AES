package com.aes.common.Enums

/**
 * Some sample crops. More crops can easily be added.
 * */
enum class Crop(val cropGroup: CropGroup, val shouldWorryAboutDrought: Boolean = false) {
    MAIZE(CropGroup.CEREAL_GRAIN, true),
    WHEAT(CropGroup.CEREAL_GRAIN),
    SOY_BEANS(CropGroup.LEGUME),
    RICE(CropGroup.CEREAL_GRAIN),
    BARLEY(CropGroup.CEREAL_GRAIN)
}
