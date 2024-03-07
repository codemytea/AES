package com.aes.notificationservice.services

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class NotificationServiceApplicationRunner (
	val eventCheckerService: EventCheckerService
): ApplicationRunner{
	override fun run(args: ApplicationArguments?) {
		eventCheckerService.checkForAllUserEvents()
	}
}