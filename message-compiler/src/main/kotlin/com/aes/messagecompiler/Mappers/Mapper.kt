package com.aes.messagecompiler.Mappers

import com.aes.common.Entities.User
import com.aes.common.Enums.Crop
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO

/**
 * Extension function to map a list of string messages to list of NewMessageDTOs
 * */
fun List<String>.toNewMessageDTO(phoneNumber : Long): List<NewMessageDTO> {
    return this.map {
        NewMessageDTO(
            message = it,
            recipient = RecipientDTO(phoneNumber)
        )
    }
}