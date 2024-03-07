package com.aes.notificationservice

import com.aes.common.Queue.LocalQueueService
import com.aes.common.Notification.entities.ScheduledNotification
import com.aes.common.Notification.repositories.NotificationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class NotificationExecuterService(
    val notificationRepository: NotificationRepository,
    val localQueueService: LocalQueueService
) {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun checkForNotifications(){
        val notificationsNotSent = notificationRepository.findAllByTimeBeforeAndAssociatedMessageIsNull(LocalDateTime.now())
        notificationsNotSent.forEach {
            sendNotification(it)
        }
    }

    fun sendNotification(notification: ScheduledNotification){
        localQueueService.writeItemToQueue("message_handler_queue", notification.toReceivedSMS())
    }
}