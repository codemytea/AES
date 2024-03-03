package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class AgriculturalQuestionExtraction : PythonClass() {

    @PythonFunction("firstLine", "AgriculturalQuestionExtraction.py")
    fun firstLine(userMessage: String):List<String?> {
        return execute(::firstLine, userMessage)
    }

}