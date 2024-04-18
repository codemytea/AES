package com.aes.common.Selling.services

import com.aes.common.Selling.entities.CropPrice
import com.aes.common.Selling.repository.CropPriceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.pow

@Service
class CropSellingService(
    val cropPriceRepository: CropPriceRepository
) {

    private fun linearJoinPoint(from: CropPrice, to: CropPrice, atDate: LocalDate): Float {
        val dPrice = to.priceLCU - from.priceLCU
        val dt = to.middleDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC) - from.middleDate().atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC)
        val dtAtDate = atDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) - from.middleDate().atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC)
        return ((dPrice / dt) * dtAtDate) + from.priceLCU
    }


    private fun linearInterpolate(atDate: LocalDate, allData: List<CropPrice>): Float {
        allData.firstOrNull { it.fromDate < atDate && it.toDate > atDate }?.let {
            return it.priceLCU
        }
        val to = allData.first {
            it.fromDate < atDate
        }
        val from = allData.last {
            it.toDate > atDate
        }
        return linearJoinPoint(from, to, atDate)
    }

    private fun linearExtrapolate(atDate: LocalDate, allData: List<CropPrice>): Float {
        val (to, from) = if (atDate < allData.last().fromDate) {
            allData[allData.size - 2] to allData[allData.size - 1]
        } else {
            allData[0] to allData[1]
        }
        return linearJoinPoint(from, to, atDate)
    }

    private fun weightedExtrapolate(atDate: LocalDate, allData: List<CropPrice>): Float {
        val list = if (atDate < allData.last().fromDate) {
            allData.reversed()
        } else {
            allData
        }
        val extrapolations = (1 until list.size).map {
            linearJoinPoint(list[0], list[it], atDate) to (atDate.atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) - list[it].middleDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
        }
        val totalSecondsAway = extrapolations.sumOf {
            it.second.toFloat().pow(2).toDouble()
        }.toFloat()
        val weightingFunction: (Float, Float) -> Double = { secondsAway, value ->
            ((secondsAway.pow(2) / totalSecondsAway) * value).toDouble()
        }
        return extrapolations.sumOf {
            weightingFunction(it.second.toFloat(), it.first)
        }.toFloat()
    }

    fun getExpectedPriceForDateInCountry(crop: String, date: LocalDate, country: String): Float {
        val allData =
            cropPriceRepository.findAllByCropNameAndCountryLike(crop, "$country%").sortedByDescending { it.fromDate }
        if (allData.isEmpty()) return 0f
        if (allData.size == 1) return allData.first().priceLCU
        return if (allData.first().toDate > date && allData.last().fromDate < date) {
            linearInterpolate(date, allData)
        } else {
            weightedExtrapolate(date, allData)
        }
    }
}