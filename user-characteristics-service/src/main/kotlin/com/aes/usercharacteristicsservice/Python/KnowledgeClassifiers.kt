package com.aes.usercharacteristicsservice.Python

import com.aes.usercharacteristicsservice.Enums.Crop
import com.aes.usercharacteristicsservice.Enums.Topic
import org.springframework.stereotype.Service

@Service
class KnowledgeClassifiers : PythonClass() {

    @PythonFunction("getCropCycleTopicsOfMessage", "knowledgeClassifier.py")
    fun getCropCycleTopicsOfMessage(messages: List<String>): Topic {
        return execute(::getCropCycleTopicsOfMessage, *messages.toTypedArray())
    }

    @PythonFunction("getCropTopicsOfMessage", "knowledgeClassifier.py")
    fun getCropTopicsOfMessage(messages: List<String>): Crop {
        return execute(::getCropTopicsOfMessage, *messages.toTypedArray())
    }
}