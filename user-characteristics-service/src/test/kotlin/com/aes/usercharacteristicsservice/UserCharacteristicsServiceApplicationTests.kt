package com.aes.usercharacteristicsservice

import com.aes.usercharacteristicsservice.Evaluators.Age.AgeEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Gender.GenderEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import com.aes.usercharacteristicsservice.Evaluators.Literacy.LiteracyEvaluator
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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


    @Test
    fun runAgeEvaluator() {
        ageEvaluator.getAgeEstimate()
    }

    @Test
    fun runGenderEvaluator() {
        genderEvaluator.getGenderEstimate()
    }

    @Test
    fun runLiteracyEvaluator() {
        literacyEvaluator.calculateLiteracyLevel()
    }

    @Test
    fun runKnowledgeEvaluator() {
        knowledgeEvaluator.calculateUserExpertiseOfTopicAndCrop()
    }

    @Test
    fun evaluateEverything() {
        runKnowledgeEvaluator()
        runLiteracyEvaluator()
        runGenderEvaluator()
        runAgeEvaluator()
    }

}
