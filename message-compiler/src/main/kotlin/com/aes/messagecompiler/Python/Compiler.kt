package com.aes.messagecompiler.Python

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service
@Service
class Compiler : PythonClass() {

    @PythonFunction("compile", "UserCharacteristicsApplied.py")
    fun compile(userMessage: String, literacyLevel: Float, gender: Gender, age : Age): String? {
        return execute(::compile, userMessage, literacyLevel, gender.toString(), age.toString())
    }

}