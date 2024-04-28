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


    fun <T> MutableList<T>.add(vararg t: T){
        this.addAll(t.toList())
    }

    val mockUser = User(
        userSmallholdingInfo = mutableListOf(
            UserSmallholding(
                location_country = "United Kingdom",
                location_city = "London",
                cashCrop = Crop.TOMATO,
                smallholdingSize = 20f
            )
        ),
        messages = mutableListOf(
            Message(
                message = "Hi, my name is Jacqueline",
                type = MessageType.INCOMING
            ),
            Message(
                message = "Hi Jacqueline! How can I assist you today?",
                type = MessageType.OUTGOING
            ),
            Message(
                message = "I would like to know how to plant tomatoes",
                type = MessageType.INCOMING
            ),
            Message(
                message = "Certainly! Just pop them in the ground and wait",
                type = MessageType.OUTGOING
            ),
        )
    )


    @Test
    fun RAGWorks() {


        val result = expertSystemService.getAgriculturalAnswer(
            "How many tomato seeds should I plant?",
            this.mockUser
        )

        println(result)
    }

    @Test
    fun answersAreRelevant(){
        val question = "How many tomato seeds should I plant?"
        val answer = expertSystemService.getAgriculturalAnswer(
            question,
            this.mockUser
        )
        pythonRagTest.testRagRelevance(listOf(question), listOf(answer))

    }
}