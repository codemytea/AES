package com.aes.common.Models

import java.time.LocalDateTime
import java.time.ZoneId

class NewMessageDTO(
    /**
     * Message to send, in english
     * */
    var message: String,
    /**
     * When to send the message - default is now + 5 seconds
     * */
    var sendtime: Int = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond().toInt() + 5,
    /**
     * The recipients of the message
     * */
    var recipients: List<RecipientDTO> = listOfNotNull(),
    /**
     * UK Shortcode all messages are sent from
     * */
    val sender: String = "447418372559",
    /**
     * Get an extra verbose response from GatewayAPI
     * */
    val extra_details: String = "recipients_usage",
)
