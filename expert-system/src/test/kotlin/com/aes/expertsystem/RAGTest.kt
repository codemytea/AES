package com.aes.expertsystem

import com.aes.expertsystem.Python.ExpertSystem
import com.aes.expertsystem.Services.ExpertSystemService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
class RAGTest {


    val expertSystemService: ExpertSystemService = ExpertSystemService(
        ExpertSystem()
    )

    @Test
    fun RAGWorks() {
        val result = expertSystemService.getAgriculturalAnswer("")
        assert(result != null)
    }
}