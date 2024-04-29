package com.aes.common.Enums

/**
 * The main crop cycle topics
 * */
enum class Topic(val notificationFrequency: Int /*days*/) {
    PESTS(1),
    DISEASES(2),
    PLANNING(3),
    SELLING(5),
    HARVEST(5),
    STORING(5),
    GROWING(5),
    BUYING_SEEDS(5),
}
