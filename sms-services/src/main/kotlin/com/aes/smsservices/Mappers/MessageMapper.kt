package com.aes.smsservices.Mappers

import com.aes.smsservices.Entities.Message
import com.aes.smsservices.Enums.MessageStatus
import com.aes.smsservices.Models.MessageDTO


fun Message.toDTO() : MessageDTO{
    return MessageDTO(
        id,
        message,
        phoneNumber,
        status ?: MessageStatus.PENDING
    )
}