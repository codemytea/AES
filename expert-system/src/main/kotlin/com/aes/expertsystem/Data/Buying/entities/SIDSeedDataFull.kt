package com.aes.expertsystem.Data.Buying.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
class SIDSeedDataFull(
    @Id
    val id: String = "",
    val int_id: Int = 0,
    val genus: String = "",
    val epithet: String = "",
    val infraspecies_epithet: String? = null,
    val authority: String? = null,
    val infraspecies_rank: String? = null,
    val infraspecies_authority: String? = null,
    val synonyms: String? = null,
    val common_name: String? = null,
    val lifeform: String? = null,
    val rank3: String? = null,
    val sp3: String? = null,
    val author3: String? = null,
    val binomial: String? = null,

    @OneToMany(cascade = [CascadeType.ALL])
    val germination: List<Germination>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val dispersal: List<Dispersal>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val storage_behaviour: List<StorageBehaviour>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val seed_weights: List<SeedWeight>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val protein_content: List<ProteinContent>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val oil_content: List<OilContent>? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val morphology: List<Morphology>? = null

) {
}