package com.aes.messagehandler.Controller

import com.aes.common.Models.MessageDTO
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagecompiler.Controller.CompilerPipeline
import com.aes.messagehandler.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.messagehandler.Information.InformationCollector
import com.aes.messagehandler.Python.AgriculturalQuestionExtraction
import com.aes.messagehandler.Python.GeneralChatbot
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class MessagePipeline(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystem: ExpertSystem,
    private val informationCollector: InformationCollector,
    private val generalChatbot: GeneralChatbot,
    private val compilerPipeline: CompilerPipeline,
) : Logging {

    /**
     * Returns a map of messages that need to be post-processed (tailored to user characteristics + limited to 255 chars) ond what type there are
     * Note: As there may be more than one agricultural question, each one is tagged agriculturalQuestionAnswer0, agriculturalQuestionAnswer1 etc
     *
     * */
    fun messagePipeline(message: MessageDTO): Map<String, List<String>> {
        logger().info("Message handler has picked up message with id ${message.id}")
        val responses = mutableMapOf<String, List<String>>()

        //try extracting the agricultural question and rest of message from user input
        getAgriculturalQuestion(message).let {

            //if a question exists
            it.first.let { questions ->
                val answers = mutableListOf<String>()
                questions.forEach { question ->
                    if (question != null) {
                        message.content = question
                        getAgriculturalAnswer(message)?.let { answer ->
                            answers.add(answer)
                        }
                    }
                }
                //try to get an answer for it, and add it to the responses array
                if (answers.isNotEmpty()){
                    responses.put("agricultural_information", answers)
                }

            }

            //with the rest of the message see if there's any useful information
            //not given to the system before.
            //save that, and if there's anything left to collect ask for it
            //remove any information from the user input and return the processed input
            collectRemainingInfo(it.second)?.let {

                //if there's anything left to collect ask for it
                it.first?.let { askForMoreInfo ->
                    responses.put("information_collection", listOf(askForMoreInfo))

                }

                it.second?.let { generalMessage ->
                    message.content = generalMessage
                }

                //pass rest of user input to general chatbot
                getGeneralAnswer(message)?.let { general ->
                    responses.put("chat", listOf(general))
                }
            }
        }

        return responses.also {
            compilerPipeline.compileMessage(it, message.phoneNumber)
        }
    }


    private fun getAgriculturalQuestion(message: MessageDTO): Pair<List<String?>, MessageDTO> {
        logger().info("Attempting to extract agricultural question from message with id ${message.id}")
        val rawExtraction = agriculturalQuestionExtraction.firstLine(message.content)

        rawExtraction.forEach {
            if (it != null) {
                message.content.replace(it, "")
            }
        }
        return Pair(rawExtraction, message)
    }


    private fun getAgriculturalAnswer(message: MessageDTO): String? {
        return expertSystem.getAgriculturalAnswer(message)
    }

    private fun collectRemainingInfo(message: MessageDTO): Pair<String?, String?>? {
        return informationCollector.askFormoreInfo(message)
    }

    private fun getGeneralAnswer(message: MessageDTO): String? {
        return generalChatbot.generalChatbot(message.content)
    }

}