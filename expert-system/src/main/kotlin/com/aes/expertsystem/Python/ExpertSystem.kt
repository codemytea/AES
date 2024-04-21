package com.aes.expertsystem.Python

import com.aes.common.Enums.UserDetails
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class ExpertSystem : PythonClass() {

    @PythonFunction("getAnswer", "RAG.py")
    fun getAnswer(userMessage: String): String {
        return execute(::getAnswer, userMessage)
    }
}