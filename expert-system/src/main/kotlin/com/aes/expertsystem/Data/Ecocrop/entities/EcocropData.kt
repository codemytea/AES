package com.aes.expertsystem.Data.Ecocrop.entities

import jakarta.persistence.*
import java.util.*

@Entity
class EcocropData(
    val scientificName: String? = null,
    val lifeform: String? = null,
    val physiology: String? = null,
    val habit: String? = null,
    val category: String? = null,
    val lifespan: String? = null,
    val plantAttributes: String? = null,
    val optimalSoilDepth: String? = null,
    val absoluteSoilDepth: String? = null,
    val optimalMinTempRequired: String? = null,
    val optimalMaxTempRequired: String? = null,
    val absoluteMinTempRequired: String? = null,
    val absoluteMaxTempRequired: String? = null,
    val optimalSoilTexture: String? = null,
    val absoluteSoilTexture: String? = null,
    val optimalMinAnnualRainfall: String? = null,
    val optimalMaxAnnualRainfall: String? = null,
    val absoluteMinAnnualRainfall: String? = null,
    val absoluteMaxAnnualRainfall: String? = null,
    val optimalSoilFertility: String? = null,
    val absoluteSoilFertility: String? = null,
    val optimalMinLatitude: String? = null,
    val optimalMaxLatitude: String? = null,
    val absoluteMinLatitude: String? = null,
    val absoluteMaxLatitude: String? = null,
    val optimalSoilAlTox: String? = null,
    val absoluteSoilAlTox: String? = null,
    val optimalMinAltitude: String? = null,
    val optimalMaxAltitude: String? = null,
    val absoluteMinAltitude: String? = null,
    val absoluteMaxAltitude: String? = null,
    val optimalSoilSalinity: String? = null,
    val absoluteSoilSalinity: String? = null,
    val optimalMinSoilPh: String? = null,
    val optimalMaxSoilPh: String? = null,
    val absoluteMinSoilPh: String? = null,
    val absoluteMaxSoilPh: String? = null,
    val optimalSoilDrainage: String? = null,
    val absoluteSoilDrainage: String? = null,
    val optimalMinLightIntensity: String? = null,
    val optimalMaxLightIntensity: String? = null,
    val absoluteMinLightIntensity: String? = null,
    val absoluteMaxLightIntensity: String? = null,

    @Lob
    @Column(length = 5000)
    val climateZone: String? = null,
    val photoPeriod: String? = null,
    val killingTempRest: String? = null,
    val killingTempEarlyGrowth: String? = null,
    val abioticTolerance: String? = null,
    val abioticSusceptibility: String? = null,
    val introductionRisks: String? = null,
    val productionSystem: String? = null,
    val minCropCycle: String? = null,
    val maxCropCycle: String? = null,
    val croppingSystem: String? = null,
    val subsystem: String? = null,


    @Lob
    @Column(length = 5000)
    val companionSpecies: String? = null,
    val mechanisationLevel: String? = null,
    val labourIntensity: String? = null,

    @OneToMany(cascade = [CascadeType.ALL])
    val uses: MutableList<EcocropUse> = mutableListOf()
) {
    @Id
    val id: String = UUID.randomUUID().toString()
}