package com.jds.aes.features.sms

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ExampleConnector(
    val smsSendService: SmsSendService
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        if(smsSendService.sendSms("07565533834", "Hi! I love you <3")){
            println("Successful send")
        }
        else{
            throw Exception("Unsuccessful send")
        }
    }

}