package com.aes.expertsystem

import com.aes.expertsystem.Models.PlantListDTO
import com.aes.expertsystem.Perenual.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ExpertSystemApplicationTests {

    @Autowired
    lateinit var controller: Controller

    @Test
    fun runAgeEvaluator() {
        controller.getSpecies(PlantListDTO(
            q="tomato"
        ))
    }

}
