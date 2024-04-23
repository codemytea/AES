package com.aes.smsservices.Mappers

import com.aes.common.Entities.Message
import com.aes.common.Enums.MessageStatus
import com.aes.common.Models.MessageDTO
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO
import com.aes.smsservices.Models.MessageStatusDTO


fun Message.toDTO(): MessageDTO {
    return MessageDTO(
        id,
        user.id,
        message,
        phoneNumber,
        status ?: MessageStatus.PENDING
    )
}

fun MessageStatusDTO.toNewMessageDTO(message : String) : NewMessageDTO {

    return NewMessageDTO(
        message = message,
        recipients = listOf(RecipientDTO(
            msisdn
        ))
    )
}
