package com.jds.aes.features.sms

import org.springframework.stereotype.Service

@Service
class SmsSendService {

    fun sendSms(number: String, message: String): Boolean{
        return true
    }

}