package com.aes.smsservices.Models.NewMessageResponse

import com.aes.common.Models.MessageDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class NewAPIMessageDTO(
    val id: Long,
    val recipients: List<NewMessageRecipientDTO>

) {
    fun toMessageDTOs(contents: String, userID: UUID): MessageDTO? {
        return recipients.firstOrNull()?.toMessageDTO(id, userID, contents)
    }
}