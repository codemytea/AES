package com.aes.servicesendsms

import java.util.*

object Utilities {
    //update with stuff from other computer.
    fun timeToUnixTimeStamp(time: Date): Int {
        println(time)
        val instant = time.toInstant()
        return instant.epochSecond.toInt()
    }
}