package com.jds.aes.features.sms

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class ExampleConnector(
    val smsSendService: SmsSendService,
    val notificationService: NotificationService,
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        //if (smsSendService.sendSms("447565533834", "Hi! I love you <3")) println("success") else println("fail")
        if (notificationService.sendNotification("447565533834", "This is a notification saying happy monthniversary!", Date(2023 - 1900, 10, 18, 10, 30))) println("success") else println("fail")

    }

}