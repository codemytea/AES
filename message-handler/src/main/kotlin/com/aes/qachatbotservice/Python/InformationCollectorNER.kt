package com.aes.qachatbotservice.Python
import com.aes.common.Enums.UserDetails
import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class InformationCollectionNER: PythonClass() {
    @PythonFunction("collect", "InformationCollectionNER.py")
    fun collect(userDetails : List<UserDetails>, message: String): Map<UserDetails, String> {
        return execute(::collect, userDetails, message)
    }
}
