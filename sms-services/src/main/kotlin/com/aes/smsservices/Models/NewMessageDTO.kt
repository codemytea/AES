package com.aes.smsservices.Models

import com.aes.smsservices.Enums.Crop
import com.aes.smsservices.Enums.Topic
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.ZoneId

class NewMessageDTO(
    /**
     * Message to send, in english
     * */
    var message: String,

    /**
     * When to send the message - default is now
     * */
    var sendtime: Int = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond().toInt() + 5,

    /**
     * List of who will receive message
     * */
    @JsonIgnore
    var recipient: RecipientDTO,

    /**
     * UK Shortcode all messages are sent from
     * */
    val sender: String = "447418372559",


    /**
     * Get an extra verbose response from GatewayAPI
     * */
    val extra_details: String = "recipients_usage",

    ){

    /**
     * The recipients of the message
     * */
    val recipients = listOf(recipient)
}