package com.aes.qachatbotservice.Python

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class AgriculturalQuestionExtraction: PythonClass() {

    @PythonFunction("firstLine", "AgriculturalQuestionExtraction.py")
    fun firstLine(userMessage: String): Pair<String?, String>? {
        return execute(::firstLine, userMessage)
    }

}