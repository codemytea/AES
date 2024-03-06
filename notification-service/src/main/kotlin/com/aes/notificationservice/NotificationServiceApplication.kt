package com.aes.notificationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExternalDataServiceApplication

fun main(args: Array<String>) {
	runApplication<ExternalDataServiceApplication>(*args)
}

//read db & find lowest knowledge area
//if planting/harvesting, check weather API and send question "will future weather affect planting/harvesting x crop"
//if yes, send notification with weather warning

//if planting/havesting && correct month for it - send notification reminder for crop
//


//scheduled
//@Scheduled

//event-based
