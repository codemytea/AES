package com.aes.smsservices.Models.NewMessageResponse

import com.aes.smsservices.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class NewAPIMessageDTO(
    val id: Long,
    val recipients: List<NewMessageRecipientDTO>

){
    fun toMessageDTOs(contents: String): List<MessageDTO>{
        return recipients.map{
            it.toMessageDTO(id, contents)
        }
    }
}