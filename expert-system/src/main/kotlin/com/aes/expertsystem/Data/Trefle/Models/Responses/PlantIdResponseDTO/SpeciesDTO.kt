package com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO

import com.aes.expertsystem.Data.Trefle.Models.Responses.LinkDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class SpeciesDTO(
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
     * API endpoints to related resources
     */
    var links: LinkDTO,

    /**
     * The plant duration(s), which can be:
     * Annual: plants that live, reproduce, and die in one growing season.
     * Biennial: plants that need two growing seasons to complete their life cycle, normally completing vegetative growth the first year and flowering the second year.
     * Perennial: plants that live for more than two years, with the shoot system dying back to soil level each year.
     */
    var duration: List<String>? = null,

    /**
     * The plant edible part(s), if any.
     */
    var edible_part: List<String>? = null,

    /**
     * Is the species edible?
     */
    var edible: Boolean? = null,

    /**
     * Is the species a vegetable?
     */
    var vegetable: Boolean? = null,

    /**
     * Some habit observations on the species
     */
    var observations: String? = null,


    /**
     * Distribution of the species per establishment
     */
    var distributions: Distributions,

    /**
     * Flower related fields (the reproductive structure found in flowering plants)
     */
    var flower: Flower,

    /**
     * Foliage (or leaves) related fields
     */
    var foliage: Foliage,

    /**
     * Fruit or seed related fields
     */
    var fruit_or_seed: FruitOrSeed,

    /**
     * Species's main characteristics
     */
    var specifications: Specifications,

    /**
     * Growing of farming related fields
     */
    var growth: Growth,

    /**
     * Any additional data
     */
    var extras: Any?
)