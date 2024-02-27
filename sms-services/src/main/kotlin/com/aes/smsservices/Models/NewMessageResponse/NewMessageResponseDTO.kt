package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageResponseDTO(

    val details: NewMessageDetailsDTO,

){
    fun toMessageDTOs(contents: String): MessageDTO?{
        return details.toMessageDTOs(contents)
    }
}