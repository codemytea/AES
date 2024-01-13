package com.aes.smsservices.Models

import com.aes.smsservices.Enums.LanguageCode
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RecievedMessageDTO(
    /**
     * The id of the incoming message. Sent by GatewayAPI
     * */
    var id: Long,

    /**
     * Phone number of user sending message
     * */
    @JsonAlias("msisdn")
    var phoneNumber: Long,

    /**
     * Message of sms
     * */
    var message: String,

    /**
     * When the message was sent
     * */
    var senttime: Int,

    /**
     * The country code of where the message was sent from
     * */
    @JsonAlias("country_code")
    var country: String
)
