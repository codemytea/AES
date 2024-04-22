package com.aes.expertsystem

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.Crop
import com.aes.common.Enums.MessageType
import com.aes.expertsystem.Python.ExpertSystem
import com.aes.expertsystem.Services.ExpertSystemService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ActiveProfiles("test")
@SpringBootTest()
class RAGTest {

    @Autowired
    lateinit var expertSystemService: ExpertSystemService


    fun <T> MutableList<T>.add(vararg t: T){
        this.addAll(t.toList())
    }

    @Test
    fun RAGWorks() {

        val mockUser = User(
            userSmallholdingInfo = mutableListOf(
                UserSmallholding(
                    location_country = "United Kingdom",
                    location_city = "London",
                    cashCrop = Crop.TOMATO
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
                    message = "Certainly! Can I ask where abouts you are located?",
                    type = MessageType.OUTGOING
                ),
                Message(
                    message = "Of course, I have a smallholding of 10 acres in South Wales",
                    type = MessageType.INCOMING
                ),
                Message(
                    message = "Thanks!",
                    type = MessageType.OUTGOING
                )
            )
        )

        val result = expertSystemService.getAgriculturalAnswer(
            "Should I plant them now",
            mockUser
        )

        println(result)
    }
}