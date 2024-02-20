package com.aes.externaldataservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExternalDataServiceApplication

fun main(args: Array<String>) {
	runApplication<ExternalDataServiceApplication>(*args)
}

//TODO weather + market api + soil type + slope for smallholding
