package com.aes.messagecompiler.Python

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class KnowledgeClassifiers : PythonClass() {

    @PythonFunction("getTopicOfMessage", "knowledgeClassifier.py")
    fun getTopicOfMessage(messages: List<String>): Topic {
        return execute(::getTopicOfMessage, messages.toTypedArray())
    }

    @PythonFunction("getCropOfMessage", "knowledgeClassifier.py")
    fun getCropOfMessage(messages: List<String>): Crop {
        return execute(::getCropOfMessage, messages.toTypedArray())
    }
}