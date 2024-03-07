package com.aes.common.Weather.geocoding

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LatLng(
    var latitude: Float = 0f,
    var longitude: Float = 0f,
    var country: String = ""
)