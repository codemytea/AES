package com.aes.common.Ecocrop.full

import com.aes.common.Ecocrop.entities.EcocropData
import com.aes.common.Ecocrop.entities.EcocropUse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

@Service
class FullPageParseService {

    private fun link(id: String) = "https://ecocrop.review.fao.org/ecocrop/srv/en/dataSheet?id=$id"

    private fun getScientificName(content: Element): String {
        return content.getElementsByTag("h2").first()!!.text()
    }

    private fun String?.format(): String? {
        return if (this.isNullOrBlank()) {
            null
        } else if (this.all { it == '-' }) null
        else this
    }

    private fun Element.findTableByTitle(title: String): Element {
        return getElementsByTag("table").first {
            it.getElementsByTag("th").any { it.text().lowercase() == title.lowercase() }
        }
    }

    private fun parsePage(document: Document): EcocropData {
        val content = document.getElementById("content")!!
        val scientificName = getScientificName(content)
        val descriptionTableChildren = content.findTableByTitle("description")
            .getElementsByTag("tr")
            .let { it.subList(1, it.size) }
            .flatMap {
                it.children()
            }
        val lifeform = descriptionTableChildren[1].text()
        val physiology = descriptionTableChildren[3].text()
        val habit = descriptionTableChildren[5].text()
        val category = descriptionTableChildren[7].text()
        val lifespan = descriptionTableChildren[9].text()
        val plantAttributes = descriptionTableChildren[11].text()

        val ecologyTableChildren = content.findTableByTitle("ecology")
            .getElementsByTag("tr")
            .let { it.subList(2, it.size) }
            .flatMap {
                it.children()
            }

        val optimalSoilDepth = ecologyTableChildren[6].text()
        val absoluteSoilDepth = ecologyTableChildren[7].text()
        val optimalMinTempRequired = ecologyTableChildren[9].text()
        val optimalMaxTempRequired = ecologyTableChildren[10].text()
        val absoluteMinTempRequired = ecologyTableChildren[11].text()
        val absoluteMaxTempRequired = ecologyTableChildren[12].text()
        val optimalSoilTexture = ecologyTableChildren[14].text()
        val absoluteSoilTexture = ecologyTableChildren[15].text()
        val optimalMinAnnualRainfall = ecologyTableChildren[17].text()
        val optimalMaxAnnualRainfall = ecologyTableChildren[18].text()
        val absoluteMinAnnualRainfall = ecologyTableChildren[19].text()
        val absoluteMaxAnnualRainfall = ecologyTableChildren[20].text()
        val optimalSoilFertility = ecologyTableChildren[22].text()
        val absoluteSoilFertility = ecologyTableChildren[23].text()
        val optimalMinLatitude = ecologyTableChildren[25].text()
        val optimalMaxLatitude = ecologyTableChildren[26].text()
        val absoluteMinLatitude = ecologyTableChildren[27].text()
        val absoluteMaxLatitude = ecologyTableChildren[28].text()
        val optimalSoilAlTox = ecologyTableChildren[30].text()
        val absoluteSoilAlTox = ecologyTableChildren[31].text()
        val optimalMinAltitude = ecologyTableChildren[33].text()
        val optimalMaxAltitude = ecologyTableChildren[34].text()
        val absoluteMinAltitude = ecologyTableChildren[35].text()
        val absoluteMaxAltitude = ecologyTableChildren[36].text()
        val optimalSoilSalinity = ecologyTableChildren[38].text()
        val absoluteSoilSalinity = ecologyTableChildren[39].text()
        val optimalMinSoilPh = ecologyTableChildren[41].text()
        val optimalMaxSoilPh = ecologyTableChildren[42].text()
        val absoluteMinSoilPh = ecologyTableChildren[43].text()
        val absoluteMaxSoilPh = ecologyTableChildren[44].text()
        val optimalSoilDrainage = ecologyTableChildren[46].text()
        val absoluteSoilDrainage = ecologyTableChildren[47].text()
        val optimalMinLightIntensity = ecologyTableChildren[49].text()
        val optimalMaxLightIntensity = ecologyTableChildren[50].text()
        val absoluteMinLightIntensity = ecologyTableChildren[51].text()
        val absoluteMaxLightIntensity = ecologyTableChildren[52].text()

        val climateTable = content.findTableByTitle("climate zone")
            .getElementsByTag("tr")
            .flatMap {
                it.children()
            }
        val climateZone = climateTable[1].text()
        val photoPeriod = climateTable[3].text()
        val killingTempRest = climateTable[5].text()
        val killingTempEarlyGrowth = climateTable[7].text()
        val abioticTolerance = climateTable[9].text()
        val abioticSusceptibility = climateTable[11].text()
        val introductionRisks = climateTable[13].text()

        val cultivationTableChildren = content.findTableByTitle("cultivation")
            .getElementsByTag("tr")
            .let { it.subList(2, it.size) }
            .flatMap {
                it.children()
            }
        val productionSystem = cultivationTableChildren[1].text()
        val minCropCycle = cultivationTableChildren[3].text()
        val maxCropCycle = cultivationTableChildren[4].text()
        val croppingSystem = cultivationTableChildren.getOrNull(10)?.text()
        val subsystem = cultivationTableChildren.getOrNull(11)?.text()
        val companionSpecies = cultivationTableChildren.getOrNull(12)?.text()
        val mechanisationLevel = cultivationTableChildren.getOrNull(13)?.text()
        val labourIntensity = cultivationTableChildren.getOrNull(14)?.text()

        val uses = content.findTableByTitle("uses")
            .getElementsByTag("tr")
            .let { it.subList(2, it.size) }
            .map {
                val data = it.getElementsByTag("td")
                EcocropUse(data[0].text().format(), data[1].text().format(), data[2].text().format())
            }

        return EcocropData(
            scientificName.format(),
            lifeform.format(),
            physiology.format(),
            habit.format(),
            category.format(),
            lifespan.format(),
            plantAttributes.format(),
            optimalSoilDepth.format(),
            absoluteSoilDepth.format(),
            optimalMinTempRequired.format(),
            optimalMaxTempRequired.format(),
            absoluteMinTempRequired.format(),
            absoluteMaxTempRequired.format(),
            optimalSoilTexture.format(),
            absoluteSoilTexture.format(),
            optimalMinAnnualRainfall.format(),
            optimalMaxAnnualRainfall.format(),
            absoluteMinAnnualRainfall.format(),
            absoluteMaxAnnualRainfall.format(),
            optimalSoilFertility.format(),
            absoluteSoilFertility.format(),
            optimalMinLatitude.format(),
            optimalMaxLatitude.format(),
            absoluteMinLatitude.format(),
            absoluteMaxLatitude.format(),
            optimalSoilAlTox.format(),
            absoluteSoilAlTox.format(),
            optimalMinAltitude.format(),
            optimalMaxAltitude.format(),
            absoluteMinAltitude.format(),
            absoluteMaxAltitude.format(),
            optimalSoilSalinity.format(),
            absoluteSoilSalinity.format(),
            optimalMinSoilPh.format(),
            optimalMaxSoilPh.format(),
            absoluteMinSoilPh.format(),
            absoluteMaxSoilPh.format(),
            optimalSoilDrainage.format(),
            absoluteSoilDrainage.format(),
            optimalMinLightIntensity.format(),
            optimalMaxLightIntensity.format(),
            absoluteMinLightIntensity.format(),
            absoluteMaxLightIntensity.format(),
            climateZone.format(),
            photoPeriod.format(),
            killingTempRest.format(),
            killingTempEarlyGrowth.format(),
            abioticTolerance.format(),
            abioticSusceptibility.format(),
            introductionRisks.format(),
            productionSystem.format(),
            minCropCycle.format(),
            maxCropCycle.format(),
            croppingSystem.format(),
            subsystem.format(),
            companionSpecies.format(),
            mechanisationLevel.format(),
            labourIntensity.format(),
            uses.toMutableList()
        )
    }

    private fun getPage(id: String): Document {
        return try {
            Jsoup.connect(link(id)).execute().parse()
        } catch (e: Throwable) {
            throw e
        }
    }

    fun getData(id: String): EcocropData {
        return try {
            val page = getPage(id)
            parsePage(page)
        } catch (e: Throwable) {
            println("Failed on id $id")
            throw e
        }
    }
}