package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Enums.MessageStatus
import com.aes.smsservices.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageRecipientDTO(
    private val country: String,
    private val msisdn: Long
){

    fun toMessageDTO(id: Long, content: String): MessageDTO{
        return MessageDTO(
            id,
            content,
            msisdn,
            MessageStatus.PENDING,
        ).apply {
           this@apply.country = this@NewMessageRecipientDTO.country
        }
    }

}