package com.aes.expertsystem

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
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

@ActiveProfiles("test")
@SpringBootTest()
class RAGTest {

    @Autowired
    lateinit var expertSystemService: ExpertSystemService

    @Test
    fun RAGWorks() {

        val mockUser = User(
            userSmallholdingInfo = mutableListOf(
                UserSmallholding(location_country = "United Kingdom")
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
                    type = MessageType.OUTGOING
                ),
                Message(
                    message = "Thanks, you can plant tomatoes now!",
                    type = MessageType.OUTGOING
                )
            )
        )

        val result = expertSystemService.getAgriculturalAnswer(
            "When is the best time to sell them?",
            mockUser
        )
        println(result)
    }
}