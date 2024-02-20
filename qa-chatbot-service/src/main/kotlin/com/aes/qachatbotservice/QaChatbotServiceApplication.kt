package com.aes.qachatbotservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QaChatbotServiceApplication

fun main(args: Array<String>) {
    runApplication<QaChatbotServiceApplication>(*args)
}


//TODO get info about user (eg name, age, gender, smallholding location, smallholding size)
