package com.aes.messagehandler.Utilities

fun String?.replaceList(list: List<String>?): String {
    var toReturn = this
    list?.forEach {
        toReturn = toReturn?.replace(it, "")
    }
    return toReturn ?: ""
}
// not working?

fun <E> List<E>?.ifNotNullOrEmpty(func: (List<E>) -> Unit): Boolean {
    return if (!this.isNullOrEmpty()) {
        func(this)
        true
    } else {
        false
    }
}
