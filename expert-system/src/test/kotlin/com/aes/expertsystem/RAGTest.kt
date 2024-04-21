package com.aes.expertsystem

import com.aes.expertsystem.Python.ExpertSystem
import com.aes.expertsystem.Services.ExpertSystemService
import org.junit.jupiter.api.Test


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