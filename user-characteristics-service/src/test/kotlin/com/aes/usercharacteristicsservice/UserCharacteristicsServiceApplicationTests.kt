package com.aes.usercharacteristicsservice

import com.aes.usercharacteristicsservice.Enums.Age
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import com.aes.usercharacteristicsservice.Enums.Gender
import org.junit.jupiter.api.Test

class UserCharacteristicsServiceApplicationTests {

    @Test
    fun pythonInterop(){
        val result = AttributeEstimator().estimateGender(listOf("Hello"))
        assert(result == Gender.MALE)
        val result2 = AttributeEstimator().estimateAge(listOf("Hello"))
        assert(result2 == Age.ADULT)
    }

}
