package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Enums.MessageStatus
import com.aes.common.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageRecipientDTO(
    private val country: String,
    private val msisdn: Long
) {

    fun toMessageDTO(id: Long, userID: UUID, content: String): MessageDTO {
        return MessageDTO(
            id,
            userID,
            content,
            msisdn,
            MessageStatus.PENDING,
        ).apply {
            this@apply.country = this@NewMessageRecipientDTO.country
        }
    }

}