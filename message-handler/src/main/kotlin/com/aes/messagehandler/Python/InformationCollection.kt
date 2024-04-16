package com.aes.messagehandler.Python

import com.aes.common.Enums.UserDetails
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class InformationCollection : PythonClass() {
//    @PythonFunction("collect", "InformationCollectionNER.py")
//    fun collect(userDetails : List<UserDetails>, message: String): Map<UserDetails, String> {
//        return execute(::collect, userDetails, message)
//    }

    /**
     * Use NER to collect information about a user
     * */
    @PythonFunction("getNewInformation", "InformationCollectionNER.py")
    fun getNewInformation(userMessage: String, userDetails: List<UserDetails>): Map<String, Any?> {
        return execute(::getNewInformation, userMessage, userDetails)
    }

    @PythonFunction("collect", "InformationCollector.py")
    fun collect(userDetails: List<UserDetails>): String? {
        return execute(::collect, userDetails)
    }
}


