package com.aes.expertsystem.ContextProvider

fun String.containsWord(word: String, ignoreCase: Boolean): Boolean {
    val tempThis = "__${this}__"
    return tempThis.contains(" ${word}__", ignoreCase)
            || tempThis.contains(" $word ", ignoreCase)
            || tempThis.contains("__$word ", ignoreCase)
            || tempThis.contains("__${word}__", ignoreCase)
}

fun String.containsAnyCardinality(other: String, ignoreCase: Boolean = false): Boolean{
    return if(other.length < 5) this.containsWord(other, ignoreCase = ignoreCase)
    else this.containsWord(other, ignoreCase = ignoreCase)
            || this.containsWord(other.substring(0, other.length - 1), ignoreCase = ignoreCase)
            || this.containsWord(other.substring(0, other.length - 2), ignoreCase = ignoreCase)
}