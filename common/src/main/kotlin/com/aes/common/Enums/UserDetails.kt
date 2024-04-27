package com.aes.common.Enums

/**
 * Key information needed about each user to provide tailoring
 * */
enum class UserDetails(val strForm: String) {
    LOCATION_CITY("LOCATION_CITY"),
    LOCATION_COUNTRY("LOCATION_COUNTRY"),
    SMALLHOLDING_SIZE("SMALLHOLDING_SIZE"),
    NAME("NAME"),
    MAIN_CROP("MAIN_CROP"),
}