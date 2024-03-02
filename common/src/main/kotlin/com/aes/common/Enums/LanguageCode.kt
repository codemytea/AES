package com.aes.common.Enums

enum class LanguageCode(val language: Language) {
    EN(Language.ENGLISH),
    FR(Language.FRENCH),
    RU(Language.RUSSIAN),
    AZ(Language.AZERBAIJANI),
    ZH(Language.CHINESE),
    CS(Language.CZECH),
    DA(Language.DANISH),
    NL(Language.DUTCH),
    FI(Language.FINNISH),
    DE(Language.GERMAN),
    EL(Language.GREEK),
    GA(Language.IRISH),
    IT(Language.ITALIAN),
    ES(Language.SPANISH),
    NONE(Language.NONE);

    companion object {
        fun fromLanguage(language: Language): LanguageCode? {
            return values().find {
                it.language == language
            }
        }
    }


}

enum class Language(val code: String) {
    RUSSIAN("ru"),
    ENGLISH("en"),
    AZERBAIJANI("az"),
    CHINESE("zh"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GREEK("el"),
    IRISH("ga"),
    ITALIAN("it"),
    SPANISH("es"),
    NONE("none")
}