package com.aes.expertsystem.Trefle.Models.Responses.PlantListResponse

import com.aes.expertsystem.Trefle.Models.Responses.LinkDTO

class PlantListResponseDataDTO(
    /**
     * An unique identifier
     */
    var id: Int? = null,

    /**
     * The usual common name, in english, of the species (if any).
     */
    var common_name: String? = null,

    /**
     * An unique human-readable identifier (if you can, prefer to use this over id)
     */
    var slug: String? = null,

    /**
     * The scientific name follows the Binomial nomenclature, and represents its genus and its species within the genus, resulting in a single worldwide name for each organism.
     */
    var scientific_name: String,

    /**
     * The first publication year of a valid name of this species.
     */
    var year: Int? = null,

    /**
     * The first publication of a valid name of this species.
     */
    var bibliography: String? = null,

    /**
     * The author(s) of the first publication of a valid name of this species.
     */
    var author: String? = null,

    /**
     * The acceptance status of this species by IPNI
     */
    var status: String,

    /**
     * The taxonomic rank of the species
     */
    var rank: String,

    /**
     * The common name (in english) of the species family
     */
    var family_common_name: String? = null,

    /**
     * The scientific name of the species family
     */
    var family: String,

    /**
     * The id of the species genus
     */
    var genus_id: Int,

    /**
     * The scientific name of the species genus
     */
    var genus: String,

    /**
     * A main image url of the species
     */
    var image_url: String? = null,

    /**
     * API endpoints to related resources
     */
    var links: LinkDTO,

    /**
     * The symonyms scientific names
     */
    var synonyms: List<String>? = null

)