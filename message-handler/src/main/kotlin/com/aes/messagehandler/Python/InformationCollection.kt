package com.aes.messagehandler.Python
import com.aes.common.Enums.UserDetails
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class InformationCollection: PythonClass() {
//    @PythonFunction("collect", "InformationCollectionNER.py")
//    fun collect(userDetails : List<UserDetails>, message: String): Map<UserDetails, String> {
//        return execute(::collect, userDetails, message)
//    }

    @PythonFunction("secondLine", "InformationRetriever.py")
    fun secondLine(userMessage : String, userDetails : List<UserDetails>): Map<String, String?> {
        return execute(::secondLine, userMessage, userDetails)
    }

    @PythonFunction("collect", "InformationCollector.py")
    fun collect(userDetails : List<UserDetails>): String? {
        return execute(::collect, userDetails)
    }
}
