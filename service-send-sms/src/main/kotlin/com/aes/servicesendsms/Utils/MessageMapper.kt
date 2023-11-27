package com.aes.servicesendsms.Utils

import com.aes.servicesendsms.Model.RecipientDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import com.aes.serviceshared.Models.MessageDTO
import com.aes.serviceshared.entities.Message

fun Message.toMessageDTO(): SendMessageDTO{
    return SendMessageDTO(
        message = this.message,
        sender = "447418372559",
        recipients = listOf(
            RecipientDTO(
                this.user!!.phoneNumber.toLong()
            )
        )
    )
}




