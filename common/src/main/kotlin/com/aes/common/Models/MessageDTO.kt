package com.aes.common.Models

import com.aes.common.Enums.MessageStatus
import java.util.*

class MessageDTO(
    /**
     * Each message has unique id. This is via an HTTP request to the webhook when there is first an update with a message.
     * This id is then used when getting and deleting specific messaged.
     * */
    val id: Long,

    /**
     * The users ID
     * */
    val userID: UUID,

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


