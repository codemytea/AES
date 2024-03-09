package com.aes.common.Notification.repositories

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.common.Notification.entities.ScheduledNotification
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ScheduledNotificationRepository: CrudRepository<ScheduledNotification, String> {
    fun findAllByTimeBeforeAndAssociatedMessageIsNull(time: LocalDateTime): List<ScheduledNotification>

    fun findFirstByCropAndTopicAndAssociatedMessageIsNull(crop: Crop, topic: Topic): ScheduledNotification?
}