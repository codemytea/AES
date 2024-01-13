package com.aes.smsservices.Models

import com.aes.smsservices.Enums.MessageStatus

class MessageDTO(
    /**
     * Each message has unique id. This is via an HTTP request to the webhook when there is first an update with a message.
     * This id is then used when getting and deleting specific messaged.
     * */
    val id: Long,

    /**
     * The message
     * */
    var content: String = "",

    /**
     * The msisdn of the recipient/sender
     * */
    val phoneNumber: Long,

    /**
     * The current status of the message
     * */
    val status: MessageStatus = MessageStatus.PENDING,
) {
    /**
     * Country message has come from. useful for setting users preferred lang if not set yet
     * */
    var country: String? = null
}


//every message has an id sent by the system
//every user has an id - their phone number

//eventually will need to translate outgoing message to specified language if gotten message from them before

//maybe piece of logic to detect country of country code of phone I'm sending the message to if first time and then send in that lang


//standard use case will be someone sending system message FIRST and then system responding.


