package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*


@JsonIgnoreProperties(ignoreUnknown = true)
class NewMessageDetailsDTO(
    val messages: List<NewAPIMessageDTO>
) {
    fun toMessageDTOs(contents: String, userID: UUID): MessageDTO? {
        return messages.firstOrNull()?.toMessageDTOs(contents, userID)
    }
}
