package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User

fun interface ContextProvider {

    fun contextForMessage(message: String, user: User): List<String>

}