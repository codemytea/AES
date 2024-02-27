package com.aes.qachatbotservice.Hub

import com.aes.common.logging.Logging
import com.aes.qachatbotservice.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.qachatbotservice.Information.InformationCollector
import com.aes.qachatbotservice.Python.AgriculturalQuestionExtraction

class MessagePipeline(
    private val agriculturalQuestionExtraction: AgriculturalQuestionExtraction,
    private val expertSystem: ExpertSystem,
    private val informationCollector: InformationCollector
) : Logging {

    fun messagePipeline(message: String): Map<String, String>? {
        val responses = mutableMapOf<String, String>()

        //try extracting the agricultural question and rest of message from user input
        getAgriculturalQuestion(message)?.let {

            //if the question exists
            it.first?.let { question ->
                //try to get an answer for it, and add it to the responses array
                getAgriculturalAnswer(question)?.let { answer -> responses.put("agriculturalQuestionAnswer", answer) }
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


    private fun getAgriculturalQuestion(message: String): Pair<String?, String>? {
        return agriculturalQuestionExtraction.firstLine(message)
    }


    private fun getAgriculturalAnswer(message: String): String? {
        return expertSystem.getAgriculturalAnswer()
    }

    private fun collectRemainingInfo(message: String): Pair<String?, String>? {
        return Pair("", "")
    }

    private fun getGeneralAnswer(message: String): String?{
        return ""
    }

}