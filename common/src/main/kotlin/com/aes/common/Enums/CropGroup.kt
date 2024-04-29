package com.aes.common.Enums

/**
 * Main crop groups. Needed to determine various blanket policies such as storage.
 * */
enum class CropGroup(val plantingType: PlantingType, val dbName: String) {
    ROOT_AND_TUBER(PlantingType.DIBBLE, "root and tuber vegetables"),
    LEAVES_OF_ROOT_AND_TUBER(PlantingType.DIBBLE, "leaves of root and tuber vegetables (human food or animal feed)"),
    BULB(PlantingType.DIBBLE, "bulb vegetable"),
    LEAFY(PlantingType.DIBBLE, "leafy vegetables (except brassica vegetables)"),
    BRASSICA(PlantingType.DIBBLE, "brassica (cole) leafy vegetables"),
    LEGUME(PlantingType.BROADCAST, "legume vegetables (succulent or dried)"),
    LEGUME_FOLIAGE(PlantingType.BROADCAST, "foliage of legume vegetables"),
    FRUITING_VEG(PlantingType.DIBBLE, "fruiting vegetables (except cucurbits)"),
    CUCURBIT(PlantingType.DIBBLE, "cucurbit vegetables"),
    CITRUS(PlantingType.DIBBLE, "citrus fruits"),
    POME(PlantingType.DIBBLE, "pome fruits"),
    STONE_FRUIT(PlantingType.DIBBLE, "stone fruits"),
    BERRY(PlantingType.DIBBLE, "berries"),
    NUT(PlantingType.DIBBLE, "tree nuts"),
    CEREAL_GRAIN(PlantingType.BROADCAST, "cereal grains"),
    FORAGE_CEREAL(PlantingType.BROADCAST, "forage, fodder and straw of cereal grains"),
    FORAGE_GRASS(PlantingType.BROADCAST, "grass forage, fodder, and hay"),
    ANIMAL_FEEDS_NON_GRASS(PlantingType.BROADCAST, "nongrass animal feeds (forage, fodder, straw and hay)"),
    OIL_SEED(PlantingType.BROADCAST, "oilseed"),
    EDIBLE_FUNGI(PlantingType.DIBBLE, "edible fungi"),
    STALK_STEM_LEAF_VEG(PlantingType.DIBBLE, "stalk, stem and leaf petiole vegetable"),
    TROPICAL_FRUIT_EDIBLE_PEEL(PlantingType.DIBBLE, "tropical and subtropical fruit, edible peel"),
    TROPICAL_FRUIT_INEDIBLE_PEEL(PlantingType.DIBBLE, "tropical and subtropical fruit, inedible peel"),
    HERBS(PlantingType.BROADCAST, "herb crop"),
    SPICES(PlantingType.BROADCAST, "spices crop"),
}
