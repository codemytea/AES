package com.jds.aes.features.sms

import com.jds.aes.Utilities
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService {
    fun sendNotification(number: String, message: String, time: Date): Boolean {
        return Utilities.sendMsg(sender = "Jabche", message= message, msisdn = number.toLong(), sendtime = Utilities.timeToUnixTimeStamp(time))
    }

}