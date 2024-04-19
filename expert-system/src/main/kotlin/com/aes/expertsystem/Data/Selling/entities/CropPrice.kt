package com.aes.expertsystem.Data.Selling.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class CropPriceId(
    val cropName: String = "",
    val country: String = "",
    val fromDate: LocalDate = LocalDate.now(),
    val toDate: LocalDate = LocalDate.now()
) : Serializable

@Entity
@IdClass(CropPriceId::class)
class CropPrice(

    @Id
    val cropName: String = "",

    @Id
    val country: String = "",

    @Id
    val fromDate: LocalDate = LocalDate.now(),

    @Id
    val toDate: LocalDate = LocalDate.now(),

    val priceLCU: Float = 0f

) {
    fun toFileLine(): String {
        return "$cropName!&!$country!&!${fromDate.format(DateTimeFormatter.ISO_DATE)}!&!${
            toDate.format(
                DateTimeFormatter.ISO_DATE
            )
        }!&!$priceLCU\n"
    }

    fun middleDate(): LocalDate {
        val startDateSecs = fromDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endDateSecs = toDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)

        val midPointSecs = (endDateSecs - startDateSecs) / 2
        return fromDate.atStartOfDay().plusSeconds(midPointSecs).toLocalDate()
    }

    companion object {

        fun fromFileLine(line: String): CropPrice {
            val items = line.split("!&!")
            return CropPrice(
                items[0],
                items[1],
                LocalDate.parse(items[2]),
                LocalDate.parse(items[3]),
                items[4].toFloat()
            )
        }
    }
}