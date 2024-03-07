package com.aes.notificationservice.event

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.notificationservice.location.Location
import java.time.LocalDateTime

interface NotificationTriggerEvent {

    fun notificationBody(crops: List<Crop>, topics: List<Topic>): String
    val executionType: ExecutionType
    val delayDays: Int

    fun shouldTriggerNotification(time: LocalDateTime, location: Location): Boolean

    fun getNotificationOrNull(time: LocalDateTime, location: Location, crops: List<Crop>, topics: List<Topic>): String?{
        return if(shouldTriggerNotification(time, location)) notificationBody(crops, topics)
        else null
    }

    fun execute(time: LocalDateTime, location: Location, crops: List<Crop>, topics: List<Topic>): List<TriggeredNotification>{
        val possibleCropsAndTopics = executionType.filterCropsAndTopics(crops, topics)
        val possibleNotification = this.getNotificationOrNull(time, location, possibleCropsAndTopics.keys.toList(), possibleCropsAndTopics.values.toList()) ?: return listOf()
        return possibleCropsAndTopics.map {
            TriggeredNotification(
                time.plusDays(delayDays.toLong()),
                it.key,
                it.value,
                possibleNotification
            )
        }

    }

}