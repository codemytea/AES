package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Notification
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NotificationRepository: CrudRepository<Notification, UUID>