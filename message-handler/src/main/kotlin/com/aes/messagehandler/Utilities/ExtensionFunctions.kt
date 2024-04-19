package com.aes.messagehandler.Utilities

fun String?.replaceList(list : List<String>?): String {
    list?.forEach {
        this?.replace(it, "")
    }
    return this ?: ""
}


fun <E> List<E>?.ifNotNullOrEmpty(func : (List<E>) -> Unit): Boolean {
    return if (!this.isNullOrEmpty()) {
        func(this)
        true
    } else {
        false
    }
}