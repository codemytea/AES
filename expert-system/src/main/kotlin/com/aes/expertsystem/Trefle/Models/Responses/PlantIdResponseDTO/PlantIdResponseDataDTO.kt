package com.aes.expertsystem.Trefle.Models.Responses.PlantIdResponseDTO

import com.aes.expertsystem.Trefle.Models.Responses.LinkDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class PlantIdResponseDataDTO(
    /**
     * An unique identifier
     */
    var id: Int,

    /**
     * The usual common name, in english, of the species (if any).
     */
    var common_name: String? = null,

    /**
     * An unique human-readable identifier (if you can, prefer to use this over id)
     */
    var slug: String,

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
     * The common name (in english) of the species family
     */
    var family_common_name: String? = null,

    /**
     * The id of the species genus
     */
    var genus_id: Int? = null,

    /**
     * The id of the main species
     */
    var main_species_id: Int? = null,

    /**
     * Indicates if the plant is a vegetable
     */
    var vegetable: Boolean? = null,

    /**
     * Observations about the plant
     */
    var observations: String? = null,

    /**
     * Main species object
     */
    var main_species: SpeciesDTO? = null,

    /**
     * Sources related to the plant
     */
    var sources: List<SourcesDTO>? = null,

    /**
     * Links related to the plant
     */
    var links: LinkDTO
)