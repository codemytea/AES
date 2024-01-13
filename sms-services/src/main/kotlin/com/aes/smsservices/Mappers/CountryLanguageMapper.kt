package com.aes.smsservices.Mappers

import net.suuft.libretranslate.Language
import java.util.*


fun getLanguageCodeForCountry(countryCode: String): Language {
    return when (countryCode.uppercase(Locale.getDefault())) {
        "RU" -> Language.RUSSIAN
        "GB" -> Language.ENGLISH
        "AZ" -> Language.AZERBAIJANI
        "CN" -> Language.CHINESE
        "CZ" -> Language.CZECH
        "DK" -> Language.DANISH
        "NL" -> Language.DUTCH
        "ZA" -> Language.DUTCH
        "SF" -> Language.FINNISH
        "FR" -> Language.FRENCH
        "DE" -> Language.GERMAN
        "GR" -> Language.GREEK
        "IE" -> Language.IRISH
        "IT" -> Language.ITALIAN
        "ES" -> Language.SPANISH
        else -> Language.NONE
    }
}
