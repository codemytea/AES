package com.aes.expertsystem

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.Crop
import com.aes.common.Enums.MessageType
import com.aes.expertsystem.Services.ExpertSystemService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest()
class RAGTest {
    @Autowired
    lateinit var expertSystemService: ExpertSystemService

    @Autowired
    lateinit var pythonRagTest: PythonRAGTest

    fun <T> MutableList<T>.add(vararg t: T) {
        this.addAll(t.toList())
    }

    val mockUser =
        User(
            userSmallholdingInfo =
                mutableListOf(
                    UserSmallholding(
                        location_country = "United Kingdom",
                        location_city = "London",
                        cashCrop = Crop.TOMATO,
                        smallholdingSize = 20f,
                    ),
                ),
            messages =
                mutableListOf(
                    Message(
                        message = "Hi, my name is John",
                        type = MessageType.INCOMING,
                    ),
                    Message(
                        message = "Hi John! How can I assist you today?",
                        type = MessageType.OUTGOING,
                    ),
                    Message(
                        message = "I would like to know how to plant tomatoes",
                        type = MessageType.INCOMING,
                    ),
                    Message(
                        message = "Certainly! Just pop them in the ground and wait",
                        type = MessageType.OUTGOING,
                    ),
                ),
        )

    @Test
    fun RAGWorks() {
        val result =
            expertSystemService.getAgriculturalAnswer(
                "How many tomato seeds should I plant?",
                this.mockUser,
            )

        println(result)
    }

    @Test
    fun answersAreRelevant() {
        val questions =
            listOf(
                "How many tomato seeds should I plant?",
                "What temperature is it best to plant aubergines at?",
                "When should I sell apples?",
                "How much could I buy orange seeds for?",
                "When should I sow wheat?",
                "Is now a good time to harvest my onions",
                "How much can I sell my apples for in a month?",
                "What is the weather like in two days? Should I be outside planting?",
                "Should I plant garlic when it is cold?",
                "How long will my olives take to mature?",
                "How many seeds do I need for 100 strawberry plants?",
                "Should I plant carrot seeds?",
                "When is the peak season for harvesting blueberries?",
                "When should I plant potatoes?",
                "What temperature is it best to plant cucumber plants?",
                "Should I grow cherries?",
                "How much can I sell my grapes for?",
                "What's the recommended spacing between tomato plants?",
                "How do I grow peppers?",
                "At what temperature should I plant lettuce at?",
                "Should I grow squash now?",
                "When do I grow watermelons?",
                "What's the average yield per acre for cultivating corn?",
                "Can I grow spinach in the winter?",
                "When should I start harvesting radishes?",
                "Should I grow melons this year?",
                "Is my soil type okay for growing barley?",
                "How long does it take for pumpkins to grow?",
                "Is now a good time to harvest tomatoes?",
                "Should I plant rosemary plants when it's really hot?",
                "Should I harvest apples now?",
                "Can I harvest strawberry plants when it's raining?",
                "Where can I grow cucumbers?",
                "How many lettuce seeds should I plant? I want to cover 3 acres.",
                "How long does it take for broccoli seeds to germinate?",
                "What's the ideal humidity level for cultivating mushrooms?",
                "What's the lifespan of a basil plant?",
                "Can I grow basil now?",
                "Is it better to grow peas or broad beans?",
                "What's the difference between determinate and indeterminate tomato varieties?",
                "What's the minimum temperature that bell peppers can tolerate?",
            )
        val answers =
            questions.map {
                expertSystemService.getAgriculturalAnswer(
                    it,
                    this.mockUser,
                )
            }
        pythonRagTest.testRagRelevance(questions, answers)
    }
}
