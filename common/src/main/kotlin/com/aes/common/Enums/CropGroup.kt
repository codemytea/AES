package com.aes.common.Enums

enum class CropGroup(val plantingType: PlantingType) {
    ROOT_AND_TUBER(PlantingType.DIBBLE),
    LEAVES_OF_ROOT_AND_TUBER(PlantingType.DIBBLE),
    BULB(PlantingType.DIBBLE),
    LEAFY(PlantingType.DIBBLE),
    BRASSICA(PlantingType.DIBBLE),
    LEGUME(PlantingType.BROADCAST),
    FORAGE_VEG(PlantingType.BROADCAST),
    FRUITING_VEG(PlantingType.DIBBLE),
    CUCURBIT(PlantingType.DIBBLE),
    CITRUS(PlantingType.DIBBLE),
    POME(PlantingType.DIBBLE),
    STONE_FRUIT(PlantingType.DIBBLE),
    BERRY(PlantingType.DIBBLE),
    NUT(PlantingType.DIBBLE),
    CEREAL_GRAIN(PlantingType.BROADCAST),
    FORAGE_CEREAL(PlantingType.BROADCAST),
    FORAGE_GRASS(PlantingType.BROADCAST),
    ANIMAL_FEEDS_NON_GRASS(PlantingType.BROADCAST),
    OIL_SEED(PlantingType.BROADCAST),
    STALK_STEM_LEAF_VEG(PlantingType.DIBBLE),
    TROPICAL_FRUIT_EDIBLE_PEEL(PlantingType.DIBBLE),
    TROPICAL_FRUIT_INEDIBLE_PEEL(PlantingType.DIBBLE),
    HERBS(PlantingType.BROADCAST),
    SPICES(PlantingType.BROADCAST)
}