package com.aes.expertsystem

import com.aes.kotlinpythoninterop.PythonClass
import com.aes.kotlinpythoninterop.PythonFunction
import org.springframework.stereotype.Service

@Service
class PythonRAGTest : PythonClass() {
    @PythonFunction("test_answer_relevance", "RAGTest.py")
    fun testRagRelevance(
        questions: List<String>,
        answers: List<String>,
    ) {
        return super.execute(::testRagRelevance, questions, answers)
    }
}
