package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class AgriculturalQuestionExtraction : PythonClass() {
    @PythonFunction("getQuestions", "AgriculturalQuestionExtraction.py")
    fun getQuestions(userMessage: String): List<String?> {
        return execute(::getQuestions, userMessage)
    }
}
