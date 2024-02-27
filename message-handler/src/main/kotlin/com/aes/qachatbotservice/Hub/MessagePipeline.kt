package com.aes.qachatbotservice.Hub

import com.aes.common.Entities.Message
import com.aes.common.logging.Logging
import com.aes.qachatbotservice.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.qachatbotservice.Information.InformationCollector
import com.aes.qachatbotservice.Python.AgriculturalQuestionExtraction

class MessagePipeline(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystem: ExpertSystem,
    private val informationCollector: InformationCollector
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
                    getAgriculturalAnswer(question)?.let { answer -> responses["agriculturalQuestionAnswer${count}"] = answer }
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

                //pass rest of user input to general chatbot
                getGeneralAnswer(it.second)?.let { general ->
                    responses.put("chat", general)
                }
            }
        }

        return if (responses.isEmpty()) null else responses
    }


    private fun getAgriculturalQuestion(message: Message): Pair<List<String>?, Message>? {
        val rawExtraction = agriculturalQuestionExtraction.firstLine(message.message)
        message.message = rawExtraction.second
    }


    private fun getAgriculturalAnswer(message: Message): String? {
        return expertSystem.getAgriculturalAnswer()
    }

    private fun collectRemainingInfo(message: Message): Pair<String?, String>? {
        return Pair("", "")
    }

    private fun getGeneralAnswer(message: Message): String?{
        return ""
    }

}