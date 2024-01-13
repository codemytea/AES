package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.Notification
import com.aes.smsservices.Entities.TimedNotifications
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TimedNotificationsRepository : CrudRepository<TimedNotifications, Notification> {
}