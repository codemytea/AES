package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.Notification
import com.aes.smsservices.Entities.NotificationTopics
import com.aes.smsservices.Entities.NotificationTopicsId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationTopicsRepository : CrudRepository<NotificationTopics, NotificationTopicsId>