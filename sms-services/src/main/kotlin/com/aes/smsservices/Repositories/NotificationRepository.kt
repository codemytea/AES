package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.Notification
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NotificationRepository : CrudRepository<Notification, UUID>