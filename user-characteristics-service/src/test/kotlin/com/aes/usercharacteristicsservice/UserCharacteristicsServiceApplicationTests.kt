package com.aes.usercharacteristicsservice

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.usercharacteristicsservice.Evaluators.Age.AgeEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Gender.GenderEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Literacy.LiteracyEvaluator
import com.aes.usercharacteristicsservice.Python.KnowledgeClassifiers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.ScheduledExecutorService

@SpringBootTest
@ActiveProfiles("test")
class UserCharacteristicsServiceApplicationTests {


    @Autowired
    lateinit var ageEvaluator: AgeEvaluator

    @Autowired
    lateinit var genderEvaluator: GenderEvaluator

    @Autowired
    lateinit var literacyEvaluator: LiteracyEvaluator

    @Autowired
    lateinit var knowledgeEvaluator: KnowledgeEvaluator

    @MockBean
    lateinit var scheduledExecutorService : ScheduledExecutorService

    @Test
    fun pythonInterop(){
        //val result = AttributeEstimator().estimateGender(listOf("Hello"))
        //assert(result == Gender.MALE)
        //val result2 = AttributeEstimator().estimateAge(listOf("Hello"))
        //assert(result2 == Age.ADULT)
        //val result3 = KnowledgeClassifiers().getCropOfMessage(listOf("How do I grow rice?"))
        //assert(result3 == Crop.RICE)
        //val result4 = KnowledgeClassifiers().getTopicOfMessage(listOf("How do I grow rice?"))
        //assert(result4 == Topic.GROWING)
    }

    @Test
    fun runAgeEvaluator(){
        ageEvaluator.getAgeEstimate()
    }

    @Test
    fun runGenderEvaluator(){
        genderEvaluator.getGenderEstimate()
    }

    @Test
    fun runLiteracyEvaluator(){
        literacyEvaluator.calculateLiteracyLevel()
    }

    @Test
    fun runKnowledgeEvaluator(){
        knowledgeEvaluator.calculateUserExpertiseOfTopicAndCrop()
    }

    @Test
    fun evaluateEverything(){
        runKnowledgeEvaluator()
        runLiteracyEvaluator()
        runGenderEvaluator()
        runAgeEvaluator()
    }

}
