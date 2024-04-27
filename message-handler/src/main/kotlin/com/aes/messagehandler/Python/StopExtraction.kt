package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service


enum class Stop(val str : String) {
    INFORMATION("INFORMATION"),
    NOTIFICATION("NOTIFICATION")
}


@Service
class StopExtraction : PythonClass() {

    @PythonFunction("getStopRequests", "StopExtractor.py")
    fun getStopRequests(userMessage: String) : List<List<String>>? {
        return execute(::getStopRequests, userMessage)
    }

}