package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Repositories.UserRepository
import com.aes.expertsystem.Services.ExpertSystemService
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.AgriculturalQuestionExtraction
import org.springframework.core.annotation.Order
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Order(1)
class AgriculturalQuestionDetector(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystemService: ExpertSystemService,
    private val userRepository: UserRepository
) : MessageHandler {

    override val messagePartType: HandlableMessageType = HandlableMessageType.AGRICULTURAL_QUESTION


    /**
     * Extracts Agricultural questions from a given message and tags them.
     *
     * @param remainingMessage the message sent by the user.
     * @param userID
     * @return a list of the agricultural questions
     * */
    override fun extractPartAndReturnRemaining(remainingMessage: String, userID: UUID): List<String>? {
        //extract the agricultural questions using OpenAI
        return agriculturalQuestionExtraction.getQuestions(remainingMessage).mapNotNull { it }.ifEmpty { null }
    }

    /**
     * Gets the answer of an agricultural question using RAG
     *
     * @param prompts contains the questions
     * @param userID
     * @return the answers to the given questions
     * */
    @Transactional
    override fun generateAnswer(prompts: List<String>, userID: UUID): List<String>? {

        val user = userRepository.findByIdOrNull(userID)!!

        return prompts.map { expertSystemService.getAgriculturalAnswer(it, user) }
    }


}