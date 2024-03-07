package com.aes.notificationservice.services

import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Notification.entities.ScheduledNotification
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.UserRepository
import com.aes.notificationservice.event.NotificationTriggerEvent
import com.aes.notificationservice.event.TriggeredNotification
import com.aes.notificationservice.location.Location
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Service
class EventCheckerService(
    val events: List<NotificationTriggerEvent>,
    val localQueueService: LocalQueueService,
    val userRepository: UserRepository
) {


    fun UserSmallholding.location(): Location{
        return Location(location_city!!, location_country!!)
    }

    @Transactional
    fun checkForAllUserEvents(){
        userRepository.findAll().forEach(::checkForUserEvents)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun checkForUserEvents(user: User){

        val crops = user.crops()
        val topics = Topic.entries.toList()
        val notifications = user.userSmallholdingInfo.flatMap {userSmallholding->
            events.flatMap {
                it.execute(LocalDateTime.now(), userSmallholding.location(), crops, topics)
            }
        }
        sendNotifications(notifications)

    }

    class ListCarrier(val list: List<NewMessageDTO>)

    fun sendNotifications(notifications: List<TriggeredNotification>){
        if(notifications.isEmpty()) return

        val messages = notifications.map {
            NewMessageDTO(
                it.message,
                it.time.atZone(ZoneId.systemDefault()).toEpochSecond().toInt()
            )
        }

        localQueueService.writeItemToQueue("send_message_queue", ListCarrier(messages))
    }


}