package com.aes.serviceshared

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceSharedApplication

fun main(args: Array<String>) {
    runApplication<ServiceSharedApplication>(*args)
}
