package com.aes.smsservices.Models.NewMessageResponse

import com.aes.smsservices.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageDetailsDTO(
    val messages: List<NewAPIMessageDTO>
){
    fun toMessageDTOs(contents: String): MessageDTO{
        return messages.flatMap { it.toMessageDTOs(contents) }
    }
}
