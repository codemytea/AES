package com.jds.aes.features.sms

import com.jds.aes.Utilities
import org.springframework.stereotype.Service

@Service
class SmsSendService {

    fun sendSms(number: String, message: String): Boolean {
        return Utilities.sendMsg(sender = "Jabche", message, msisdn = number.toLong())
    }

}