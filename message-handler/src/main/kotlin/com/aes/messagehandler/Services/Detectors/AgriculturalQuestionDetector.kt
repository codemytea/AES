package com.aes.messagehandler.Services.Detectors

import com.aes.common.Models.TaggingMessage
import com.aes.common.Queue.LocalQueueService
import com.aes.messagehandler.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.common.Enums.HandlableMessageType
import com.aes.messagehandler.MessageHandler
import com.aes.messagehandler.Python.AgriculturalQuestionExtraction
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Service
@Order(1)
class AgriculturalQuestionDetector(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystem: ExpertSystem,
    private val localQueueService: LocalQueueService,
) : MessageHandler {

    override val messagePartType: HandlableMessageType = HandlableMessageType.AGRICULTURAL_QUESTION


    /**
     * Extracts Agricultural questions from a given message and tags them.
     *
     * @param remainingMessage the message sent by the user.
     * @param userID
     * @return a list of the agricultural questions
     * */
    override fun detectMessagePartType(remainingMessage: String, userID: UUID): List<String>? {
        //extract the agricultural questions using OpenAI
        return agriculturalQuestionExtraction.getQuestions(remainingMessage).mapNotNull { it }.ifEmpty { null }
            ?.also { tagIncomingMessage(it, userID) }
    }

    /**
     * Gets the answer of an agricultural question using RAG
     *
     * @param prompts contains the questions
     * @param userID
     * @return the answers to the given questions
     * */
    override fun generateAnswer(prompts: List<String>, userID: UUID): List<String>? {
        return prompts.map { expertSystem.getAgriculturalAnswer(it) }
    }

    /**
     * Tags the agricultural question(s) in the message by adding questions to the message_tag_queue.
     * This is ultimately used to calculate a users knowledge in a given area.
     *
     * @param questions a list of agricultural questions
     * @param userID
     * */
    private fun tagIncomingMessage(questions: List<String>, userID: UUID) {
        //zips the questions together
        val zippedQuestions = questions.joinToString(" ")

        //writes to the queue
        localQueueService.writeItemToQueue("message_tag_queue", TaggingMessage(zippedQuestions, userID))
    }

}