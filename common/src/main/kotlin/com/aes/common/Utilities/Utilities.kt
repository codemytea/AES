package com.aes.common.Utilities

import com.aes.common.Enums.TimeIdentifiers
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

object Utilities {
    fun makeDate(
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue,
        day: Int = LocalDate.now().dayOfMonth,
        hour: Int = LocalDateTime.now().hour,
        minute: Int = LocalDateTime.now().minute,
        second: Int = LocalDateTime.now().second,
    ): LocalDateTime {
        return LocalDateTime.of(year, month, day, hour, minute, second)
    }

    fun timeToUnixTimeStamp(time: LocalDateTime, offset: ZoneOffset = ZoneOffset.UTC): Int {
        return time.toEpochSecond(offset).toInt()
    }

    fun convertIntervalAndAdd(currTime: LocalDateTime, interval: TimeIdentifiers, plusTime: Int): LocalDateTime {
        return when (interval) {
            TimeIdentifiers.YEAR -> currTime.plusYears(plusTime.toLong())
            TimeIdentifiers.MONTH -> currTime.plusMonths(plusTime.toLong())
            TimeIdentifiers.DAY -> currTime.plusDays(plusTime.toLong())
            TimeIdentifiers.HOUR -> currTime.plusHours(plusTime.toLong())
            TimeIdentifiers.MINUTE -> currTime.plusMinutes(plusTime.toLong())
            TimeIdentifiers.SECOND -> currTime.plusSeconds(plusTime.toLong())
        }
    }
}