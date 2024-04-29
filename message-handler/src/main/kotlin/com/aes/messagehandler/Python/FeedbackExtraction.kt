package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class FeedbackExtraction : PythonClass() {
    @PythonFunction("getFeedback", "FeedbackExtractor.py")
    fun getFeedback(userMessage: String): List<String?> {
        return execute(::getFeedback, userMessage)
    }
}
