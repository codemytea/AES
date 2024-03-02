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

fun Language.toStandardLanguage() : com.aes.common.Enums.Language {
   return when(this){
       Language.RUSSIAN -> com.aes.common.Enums.Language.RUSSIAN
       Language.ENGLISH -> com.aes.common.Enums.Language.ENGLISH
       Language.AZERBAIJANI -> com.aes.common.Enums.Language.AZERBAIJANI
       Language.CHINESE -> com.aes.common.Enums.Language.CHINESE
       Language.CZECH -> com.aes.common.Enums.Language.CZECH
       Language.DANISH -> com.aes.common.Enums.Language.DANISH
       Language.DUTCH -> com.aes.common.Enums.Language.DUTCH
       Language.FINNISH -> com.aes.common.Enums.Language.FINNISH
       Language.GERMAN -> com.aes.common.Enums.Language.GERMAN
       Language.IRISH -> com.aes.common.Enums.Language.IRISH
       Language.ITALIAN -> com.aes.common.Enums.Language.ITALIAN
       Language.SPANISH -> com.aes.common.Enums.Language.SPANISH
       else -> com.aes.common.Enums.Language.NONE
   }
}

fun com.aes.common.Enums.Language.toLibreLanguage() : Language {
    return when(this){
        com.aes.common.Enums.Language.RUSSIAN -> Language.RUSSIAN
        com.aes.common.Enums.Language.ENGLISH -> Language.ENGLISH
        com.aes.common.Enums.Language.AZERBAIJANI -> Language.AZERBAIJANI
        com.aes.common.Enums.Language.CHINESE -> Language.CHINESE
        com.aes.common.Enums.Language.CZECH -> Language.CZECH
        com.aes.common.Enums.Language.DANISH -> Language.DANISH
        com.aes.common.Enums.Language.DUTCH -> Language.DUTCH
        com.aes.common.Enums.Language.FINNISH -> Language.FINNISH
        com.aes.common.Enums.Language.GERMAN -> Language.GERMAN
        com.aes.common.Enums.Language.IRISH -> Language.IRISH
        com.aes.common.Enums.Language.ITALIAN -> Language.ITALIAN
        com.aes.common.Enums.Language.SPANISH -> Language.SPANISH
        else -> Language.NONE
    }
}
