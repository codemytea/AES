package com.aes.smsservices.Models

import com.aes.smsservices.Enums.LanguageCode

class TranslateMessageDTO (
    /**
     * The id of the message being translated, set by GatewayAPI
     * */
    val id: Int? = null,

    /**
     * The message in the language which will need to be translated
     * */
    val messageContent: String? = null,
)