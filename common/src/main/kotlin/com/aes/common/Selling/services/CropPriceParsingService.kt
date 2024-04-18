package com.aes.common.Selling.services

import com.aes.common.Selling.entities.CropPrice
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDate

@Service
open class CropPriceParsingService {

    companion object {
        const val AREA_INDEX = 2
        const val ITEM_INDEX = 5
        const val YEAR_INDEX = 9
        const val MONTHS_INDEX = 11
        const val UNIT_INDEX = 12
        const val VALUE_INDEX = 13

    }

    enum class FAOSTATMonth() {
        Annualvalue,
        January,
        February,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December;

        fun toDateRange(year: Int): Pair<LocalDate, LocalDate> {
            return if (this == Annualvalue) {
                LocalDate.of(year, 1, 1) to LocalDate.of(year, 12, 31)
            } else {
                val start = LocalDate.of(year, this.ordinal, 1)
                start to start.plusMonths(1).minusDays(1)
            }
        }
    }

    private fun String.toMonth(): FAOSTATMonth {
        return FAOSTATMonth.valueOf(this.replace(" ", ""))
    }

    fun compressData(inputFile: File, outputFile: File) {
        inputFile.forEachLine {
            parseSingleLine(it)?.let {
                outputFile.appendText(it.toFileLine())
            }
        }
    }


    fun parseSingleLine(line: String): CropPrice? {
        val items = line.removePrefix("\"").split(",\"").map { it.substring(0, it.length - 1) }
        val unit = items[UNIT_INDEX]

        if (unit != "LCU") return null

        val area = items[AREA_INDEX]
        val item = items[ITEM_INDEX]
        val year = items[YEAR_INDEX].toInt()
        val (startDate, endDate) = items[MONTHS_INDEX].toMonth().toDateRange(year)
        val value = items[VALUE_INDEX].toFloat()

        return CropPrice(item, area, startDate, endDate, value)
    }
}