package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Notification
import com.aes.serviceshared.entities.TimedNotifications
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TimedNotificationsRepository : CrudRepository<TimedNotifications, Notification> {
}