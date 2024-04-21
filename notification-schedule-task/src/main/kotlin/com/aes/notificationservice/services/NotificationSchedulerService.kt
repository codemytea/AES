package com.aes.notificationservice.services

import com.aes.common.Entities.User
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Repositories.UserRepository
import com.aes.common.Notification.entities.ScheduledNotification
import com.aes.common.Notification.repositories.ScheduledNotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class NotificationSchedulerService(
    val scheduledNotificationRepository: ScheduledNotificationRepository,
    val userRepository: UserRepository
) {


    @Transactional
    fun generateAllNotifications(){
        userRepository.findAll().forEach(::generateNotificationsForUser)
    }

    fun generateNotificationsForUser(user: User){
        for (crop in user.crops()) {
            for(topic in Topic.values()){
                scheduleNotification(
                    user,
                    LocalDateTime.now().plusDays(topic.notificationFrequency.toLong()),
                    crop,
                    topic
                )
            }
        }
    }


    fun scheduleNotification(user: User, time: LocalDateTime, crop: Crop, topic: Topic){
        scheduledNotificationRepository.findFirstByCropAndTopicAndAssociatedMessageIsNull(crop, topic)?.let {
            return
        }
        val notification = ScheduledNotification(
            UUID.randomUUID(),
            time,
            user,
            crop,
            topic,
            "Tell me more about ${topic.name.lowercase()} for ${crop.name.lowercase()}"
        )
        scheduledNotificationRepository.save(notification)
    }


}