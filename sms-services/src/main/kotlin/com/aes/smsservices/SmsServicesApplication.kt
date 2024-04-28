package com.aes.smsservices

import com.aes.smsservices.Configuration.SmsServiceConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.aes"])
@EntityScan(basePackages = ["com.aes"])
@ComponentScan(basePackages = ["com.aes"])
@EnableScheduling
@EnableConfigurationProperties(SmsServiceConfiguration::class)
class SmsServicesApplication

fun main(args: Array<String>) {
    runApplication<SmsServicesApplication>(*args)
}

