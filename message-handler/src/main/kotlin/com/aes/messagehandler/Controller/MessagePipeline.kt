package com.aes.messagehandler.Controller

import com.aes.common.Entities.Message
import com.aes.common.Enums.MessageStatus
import com.aes.common.Models.MessageDTO
import com.aes.common.Queue.LocalQueueService
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagecompiler.Controller.CompilerPipeline
import com.aes.messagehandler.AgriculturalQuestionAnswerer.ExpertSystem.ExpertSystem
import com.aes.messagehandler.Information.InformationCollector
import com.aes.messagehandler.Mappers.TaggingMessage
import com.aes.messagehandler.Mappers.toDTO
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
    private val localQueueService: LocalQueueService,
) : Logging {

    /**
     * Returns a map of messages that need to be post-processed (tailored to user characteristics + limited to 255 chars)
     * ond what type there are.
     * Note: As there may be more than one agricultural question, each one is tagged agriculturalQuestionAnswer0,
     * agriculturalQuestionAnswer1 etc
     *
     * @param message - the messageDTO of the incoming message
     * @return a map of the type of content eg 'chat' and the message associated with that eg 'who are you?'
     * */
    fun messagePipeline(message: Message): Map<String, List<String>> {
        logger().info("Message handler has picked up message with id ${message.id}")
        val responses = mutableMapOf<String, List<String>>()
        val dtodMessage = message.toDTO()

        //try extracting the agricultural question and rest of message from user input
        getAgriculturalQuestion(dtodMessage).let {

            //if questions exist
            it.first.let { questions ->
                val answers = mutableListOf<String>()
                //tag messages
                tagIncomingMessage(questions, message.id)
                //and find the answer to each question
                questions.forEach { question ->
                    if (question != null) {
                        dtodMessage.content = question
                        getAgriculturalAnswer(dtodMessage)?.let { answer ->
                            answers.add(answer)
                        }
                    }
                }
                //if there are any answers, add them to the overall message to return
                if (answers.isNotEmpty()){
                    responses.put("2-agricultural_information", answers)
                }

            }

            //with the rest of the message see if there's any useful information
            //not given to the system before.
            //save that, and if there's anything left to collect ask for it
            //remove any information from the user input and return the processed input
            collectRemainingInfo(it.second)?.let {

                //if there's anything left to collect ask for it
                it.first?.let { askForMoreInfo ->
                    responses.put("3-information_collection", listOf(askForMoreInfo))

                }

                it.second?.let { generalMessage ->
                    dtodMessage.content = generalMessage
                }

                //pass rest of user input to general chatbot
                getGeneralAnswer(dtodMessage)?.let { general ->
                    responses.put("1-chat", listOf(general))
                }
            }
        }

        return responses.also {
            compilerPipeline.compileMessage(it, dtodMessage.phoneNumber)
        }
    }

    /**
     * Tags the agricultural question(s) in the message by adding questions to the message_tag_queue.
     * This is ultimately used to calculate a users knowledge in a given area.
     *
     * @param questions - a list of agricultural questions
     * @param messageID - the ID of the incoming message (to store Knowledge Areas in the DB)
     * */
    private fun tagIncomingMessage(questions : List<String?>, messageID : Long) {
        //zips the questions together
        val zippedQuestions = questions.mapNotNull {
            it
        }.joinToString(" ")

        //writes to the queue
        localQueueService.writeItemToQueue("message_tag_queue", TaggingMessage(zippedQuestions, messageID))
    }


    /**
     * Extracts Agricultural questions from a given message and removes them from it.
     *
     * @param message - the message sent by the user.
     * @return a pair of the list of extracted agricultural questions, and the leftover message with the questions removed.
     * */
    private fun getAgriculturalQuestion(message: MessageDTO): Pair<List<String?>, MessageDTO> {
        logger().info("Attempting to extract agricultural questions from message with id ${message.id}")

        //extract the agricultural questions using OpenAI
        val rawExtraction = agriculturalQuestionExtraction.getQuestions(message.content)

        //remove agricultural questions from the users message
        rawExtraction.forEach {
            if (it != null) {
                message.content.replace(it, "")
            }
        }

        logger().info("Extracted the following agricultural questions for message with id ${message.id}: $rawExtraction")
        logger().info("After extracting agricultural questions for message with id ${message.id} the left over message is: ${message.content}")

        return Pair(rawExtraction, message)
    }


    /**
     * Gets the answer of an agricultural question using RAG
     *
     * @param message - contains various metadata of the question. The question itself is message.contents
     * @return the answer to the given question
     * */
    private fun getAgriculturalAnswer(message: MessageDTO): String? {
        logger().info("Getting agricultural answer for question ${message.content} for user with id ${message.userID}")
        return expertSystem.getAgriculturalAnswer(message)
    }

    /**
     * Calls function which checks if the message has any new information about the user.
     * If the user hasn't asked for the system to stop information collection, also determines what other information
     * about the user is still missing, and compiles a question asking the user to provide that information.
     *
     * @param message - the message without agricultural questions
     * @return a pair of the message asking for more information, and the original user message after any bits
     *          to do with giving more information have been removed.
     * */
    private fun collectRemainingInfo(message: MessageDTO): Pair<String?, String?>? {
        logger().info("Collecting information for user with id ${message.userID}")
        return informationCollector.askFormoreInfo(message)
    }

    private fun getGeneralAnswer(message: MessageDTO): String? {
        return generalChatbot.generalChatbot(message.content).also{
            logger().info("General chat for user with id ${message.userID} is $it")
        }
    }

}