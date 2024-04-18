package com.aes.common.Trefle.Models.Responses.PlantIdResponseDTO

import com.aes.common.Trefle.Models.Responses.LinkDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class DistributionZone(
    /**
     * An unique identifier
     */
    var id: Int,

    /**
     * The zone name
     */
    var name: String,

    /**
     * An unique, human readable, identifier
     */
    var slug: String,

    /**
     * The TDWG zone unique code
     */
    var tdwg_code: String,

    /**
     * The TDWG zone level
     */
    var tdwg_level: Int,

    /**
     * The number of species in this zone
     */
    var species_count: Int,

    var links: LinkDTO
)