package com.aes.smsservices.Models

import com.fasterxml.jackson.annotation.JsonProperty

class RecipientDTO(
    /**
     * Phone number to which message is sent to
     * */
    @JsonProperty("msisdn")
    var phoneNumber: Long,
)
