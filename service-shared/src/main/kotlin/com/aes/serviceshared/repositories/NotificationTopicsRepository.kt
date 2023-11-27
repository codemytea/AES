package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Notification
import com.aes.serviceshared.entities.NotificationTopics
import com.aes.serviceshared.entities.NotificationTopicsId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationTopicsRepository : CrudRepository<NotificationTopics, NotificationTopicsId> {
}