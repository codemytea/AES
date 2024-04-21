package com.aes.messagehandler.Python

import com.aes.common.Enums.UserDetails
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class InformationCollection : PythonClass() {

    /**
     * Use NER to collect information about a user
     * */
    @PythonFunction("getNewInformation", "InformationCollectionNER.py")
    fun getNewInformation(userMessage: String, userDetails: List<UserDetails>): Map<UserDetails, String> {
        return execute(::getNewInformation, userDetails, userMessage)
    }

    @PythonFunction("collect", "InformationCollector.py")
    fun collect(userDetails: List<UserDetails>): String? {
        return execute(::collect, userDetails)
    }

    @PythonFunction("removeNewInformation", "InformationRetriever.py")
    fun removeNewInformation(userMessage: String, userDetails: List<UserDetails>): String {
        return execute(::removeNewInformation, userMessage, userDetails)
    }
}


