package com.aes.notificationservice.event

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import java.time.LocalDateTime

class TriggeredNotification(
    val time: LocalDateTime,
    val crop: Crop,
    val topic: Topic,
    val message: String
)