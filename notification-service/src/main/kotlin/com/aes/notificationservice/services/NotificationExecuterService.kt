package com.aes.notificationservice.services

import com.aes.common.Queue.LocalQueueService
import com.aes.common.Notification.entities.ScheduledNotification
import com.aes.common.Notification.repositories.ScheduledNotificationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class NotificationExecuterService(
    val scheduledNotificationRepository: ScheduledNotificationRepository,
    val localQueueService: LocalQueueService
) {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun checkForNotifications(){
        val notificationsNotSent = scheduledNotificationRepository.findAllByTimeBeforeAndAssociatedMessageIsNull(LocalDateTime.now())
        notificationsNotSent.forEach {
            sendNotification(it)
        }
    }

    fun sendNotification(notification: ScheduledNotification){
        localQueueService.writeItemToQueue("message_handler_queue", notification.toReceivedSMS())
    }
}