package com.aes.notificationservice.services

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.aes"])
@EntityScan(basePackages = ["com.aes"])
@ComponentScan(basePackages = ["com.aes"])
class NotificationTaskApplication

fun main(args: Array<String>) {
    runApplication<NotificationTaskApplication>(*args)
}

// read db & find lowest knowledge area
// if planting/harvesting, check weather API and send question "will future weather affect planting/harvesting x crop"
// if yes, send notification with weather warning

// if planting/havesting && correct month for it - send notification reminder for crop
//

// scheduled
// @Scheduled

// event-based
