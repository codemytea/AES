package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*


@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageResponseDTO(

    val details: NewMessageDetailsDTO,

){
    fun toMessageDTOs(contents: String, userID: UUID): MessageDTO?{
        return details.toMessageDTOs(contents, userID)
    }
}