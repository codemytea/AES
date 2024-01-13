package com.aes.smsservices.Services

import org.springframework.stereotype.Service
import com.aes.common.logging.Logging
import com.aes.smsservices.Enums.LanguageCode
import com.aes.smsservices.Models.MessageDTO
import com.aes.smsservices.Models.TranslateMessageDTO
import net.suuft.libretranslate.Translator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Service
class TranslateSmsService : Logging {


    fun translateMessage(contents: String, toLanguage: LanguageCode = LanguageCode.EN, fromLanguage: LanguageCode = LanguageCode.EN) : String {
        return if(fromLanguage == toLanguage) contents
        else Translator.translate(
            fromLanguage.language,
            toLanguage.language,
            contents
        )
    }
}