package com.aes.notificationservice

import com.aes.notificationservice.services.NotificationSchedulerService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class NotificationTaskApplicationRunner(
    val notificationSchedulerService: NotificationSchedulerService
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        notificationSchedulerService.generateAllNotifications()
    }
}