package com.aes.common.Trefle.Models.Responses

class LinkDTO(
    /**
     * API endpoint to the species itself
     * */
    val self: String?,

    /**
     * API endpoint to the species genus
     * */
    val genus: String?,

    /**
     * API endpoint to the species plant
     * */
    val plant: String?
)