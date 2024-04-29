package com.aes.messagecompiler.Python

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class Compiler : PythonClass() {
    @PythonFunction("userCharacteristicCompiling", "UserCharacteristicsApplied.py")
    fun userCharacteristicCompiling(
        userMessage: String,
        literacyLevel: Float,
        gender: Gender,
        age: Age,
    ): String? {
        return execute(::userCharacteristicCompiling, userMessage, literacyLevel, gender.toString(), age.toString())
    }

    @PythonFunction("userKnowledgeCompiling", "UserKnowledgeApplied.py")
    fun userKnowledgeCompiling(
        userMessage: String,
        knowledgeLevel: Double,
    ): String? {
        return execute(::userKnowledgeCompiling, userMessage, knowledgeLevel)
    }
}
