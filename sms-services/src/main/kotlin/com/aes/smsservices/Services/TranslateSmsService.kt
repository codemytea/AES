package com.aes.smsservices.Services

import com.aes.common.Enums.LanguageCode
import com.aes.common.logging.Logging
import com.aes.smsservices.Mappers.toLibreLanguage
import net.suuft.libretranslate.Translator
import org.springframework.stereotype.Service

@Service
class TranslateSmsService : Logging {


    fun translateMessage(
        contents: String,
        toLanguage: LanguageCode = LanguageCode.EN,
        fromLanguage: LanguageCode = LanguageCode.EN
    ): String {
        return if (fromLanguage == toLanguage) contents
        else Translator.translate(
            fromLanguage.language.toLibreLanguage(),
            toLanguage.language.toLibreLanguage(),
            contents
        )
    }
}