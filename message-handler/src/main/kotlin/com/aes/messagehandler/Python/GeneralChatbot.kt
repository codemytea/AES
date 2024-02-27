package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class GeneralChatbot : PythonClass() {

    @PythonFunction("generalChatbot", "GeneralChatbot.py")
    fun generalChatbot(userMessage: String): String? {
        return execute(::generalChatbot, userMessage)
    }

}