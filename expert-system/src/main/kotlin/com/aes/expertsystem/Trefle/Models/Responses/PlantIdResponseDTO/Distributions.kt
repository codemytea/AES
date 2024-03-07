package com.aes.expertsystem.Trefle.Models.Responses.PlantIdResponseDTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class Distributions (
    /**
     * Zones the species is native from
     */
    var native: List<DistributionZone>?,

    /**
     * Zones the species has been introduced
     */
    var introduced: List<DistributionZone>?,

    /**
     * Zones the species presence is doubtful
     */
    var doubtful: List<DistributionZone>?,

    /**
     * Zones the species is absent and has been wrongly recorded
     */
    var absent: List<DistributionZone>?,

    /**
     * Zones the species is extinct
     */
    var extinct: List<DistributionZone>?
)