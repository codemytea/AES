package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service


enum class Stop {
    INFORMATION,
    NOTIFICATION
}


@Service
class StopExtraction : PythonClass() {

    @PythonFunction("getStopRequests", "StopExtractor.py")
    fun getStopRequests(userMessage: String) : List<Pair<String, Stop>>? {
        return execute(::getStopRequests, userMessage)
    }

}