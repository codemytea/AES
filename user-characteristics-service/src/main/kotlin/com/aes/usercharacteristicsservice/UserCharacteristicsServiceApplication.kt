package com.aes.usercharacteristicsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.aes"])
@EntityScan(basePackages = ["com.aes"])
@ComponentScan(basePackages = ["com.aes"])
@EnableScheduling
class UserCharacteristicsServiceApplication

fun main(args: Array<String>) {
    runApplication<UserCharacteristicsServiceApplication>(*args)
}
