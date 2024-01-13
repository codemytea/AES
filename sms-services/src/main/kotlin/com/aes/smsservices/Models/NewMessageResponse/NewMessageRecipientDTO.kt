package com.aes.smsservices.Models.NewMessageResponse

import com.aes.smsservices.Enums.MessageStatus
import com.aes.smsservices.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageRecipientDTO(
    val country: String,
    val msisdn: Long
){

    fun toMessageDTO(id: Long, content: String): MessageDTO{
        return MessageDTO(
            id,
            content,
            msisdn,
            MessageStatus.PENDING,
            country
        )
    }

}