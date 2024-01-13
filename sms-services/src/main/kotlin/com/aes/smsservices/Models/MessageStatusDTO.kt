package com.aes.smsservices.Models

import com.aes.smsservices.Enums.MessageStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class MessageStatusDTO(

    /**
     * The system set id of the message
     * */
    val id: Long,

    /**
     * The phone number associated with the message
     * */
    val msisdn: Long,


    /**
     * The status of the message
     * */
    val status: MessageStatus
)
