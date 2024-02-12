package com.aes.usercharacteristicsservice.Python

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import org.springframework.stereotype.Service

@Service
class AttributeEstimator: PythonClass() {


    @PythonFunction("getAgeForMessages", "attributeEstimator.py")
    fun estimateAge(messages: List<String>): Age {
       return execute(::estimateAge, messages.toTypedArray())
    }

    @PythonFunction("getGenderForMessages", "attributeEstimator.py")
    fun estimateGender(messages: List<String>): Gender {
        return execute(::estimateGender, messages.toTypedArray())
    }


}