package com.aes.messagehandler.Hub

import com.aes.common.Models.MessageDTO
import com.aes.common.logging.Logging
import com.aes.messagehandler.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.messagehandler.Information.InformationCollector
import com.aes.messagehandler.Python.AgriculturalQuestionExtraction
import com.aes.messagehandler.Python.GeneralChatbot
import org.springframework.stereotype.Service

@Service
class MessagePipeline(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystem: ExpertSystem,
    private val informationCollector: InformationCollector,
    private val generalChatbot: GeneralChatbot,
) : Logging {

    /**
     * Returns a map of messages that need to be post-processed (tailored to user characteristics + limited to 255 chars) ond what type there are
     * Note: As there may be more than one agricultural question, each one is tagged agriculturalQuestionAnswer0, agriculturalQuestionAnswer1 etc
     *
     * */
    fun messagePipeline(message: MessageDTO): Map<String, String>? {
        val responses = mutableMapOf<String, String>()

        var count = 0

        //try extracting the agricultural question and rest of message from user input
        getAgriculturalQuestion(message)?.let {

            //if the question exists
            it.first?.let { questions ->
                questions.forEach { question ->
                    message.content = question
                    getAgriculturalAnswer(message)?.let { answer -> responses["agriculturalQuestionAnswer${count}"] = answer }
                    count++
                }
                //try to get an answer for it, and add it to the responses array

            }

            //with the rest of the message see if there's any useful information
            //not given to the system before.
            //save that, and if there's anything left to collect ask for it
            //remove any information from the user input and return the processed input
            collectRemainingInfo(it.second)?.let {

                //if there's anything left to collect ask for it
                it.first?.let { askForMoreInfo ->
                    responses.put("informationCollection", askForMoreInfo)

                }

                it.second?.let { generalMessage ->
                     message.content = generalMessage
                }

                //pass rest of user input to general chatbot
                getGeneralAnswer(message)?.let { general ->
                    responses.put("chat", general)
                }
            }
        }

        return if (responses.isEmpty()) null else responses
    }


    private fun getAgriculturalQuestion(message: MessageDTO): Pair<List<String>?, MessageDTO> {
        val rawExtraction = agriculturalQuestionExtraction.firstLine(message.content)
        if (rawExtraction != null) {
            message.content = rawExtraction.second
        }
        return Pair(rawExtraction?.first, message)
    }


    private fun getAgriculturalAnswer(message: MessageDTO): String? {
        return expertSystem.getAgriculturalAnswer()
    }

    private fun collectRemainingInfo(message: MessageDTO): Pair<String?, String?>? {
        return informationCollector.askFormoreInfo(message)
    }

    private fun getGeneralAnswer(message: MessageDTO): String?{
        return generalChatbot.generalChatbot(message.content)
    }

}