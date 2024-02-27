package com.aes.smsservices.Mappers

import com.aes.common.Entities.Message
import com.aes.common.Enums.MessageStatus
import com.aes.common.Models.MessageDTO


fun Message.toDTO(): MessageDTO {
    return MessageDTO(
        id,
        user.id,
        message,
        phoneNumber,
        status ?: MessageStatus.PENDING
    )
}