package com.aes.messagehandler.Python

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class ReferAFriendExtraction : PythonClass() {

    @PythonFunction("getReferral", "ReferralExtractor.py")
    fun getReferral(userMessage: String):List<String?> {
        return execute(::getReferral, userMessage)
    }

}