package com.aes.messagecompiler.Controller

import com.aes.common.Entities.User
import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagecompiler.Mappers.toNewMessageDTO
import com.aes.messagecompiler.Python.Compiler
import org.springframework.stereotype.Service


@Service
class CompilerPipeline(
    val compiler: Compiler,
    val userRepository: UserRepository,
    val localQueueService: LocalQueueService,
    val messageTaggingService: MessageTaggingService
) : Logging {


    /**
     * Compiles a message by tweaking it to the users characteristics and then chunking/amalgamating them to one
     * message long chunks. A
     *
     * @param initialResponse - an initial list of responses with what they correspond to
     * @param phoneNumber - the receiving users' phone number
     * @return list of NewMessageDTOs ready to be sent
     * */
    fun compileMessage(initialResponse : Map<HandlableMessageType, List<String>>, phoneNumber : Long) : List<NewMessageDTO>? {
        logger().info("Compiling message for user with phone number $phoneNumber")
        var improved :  Map<HandlableMessageType, List<String>>? = null
        userRepository.findByPhoneNumberContaining(phoneNumber)?.let {
            improved = improveSuggestability(initialResponse, it) //if a user with information exists, improve the message
        }

        return finalSplit(improved ?: initialResponse).toNewMessageDTO(phoneNumber).also {
            logger().info("Putting message for user with phone number $phoneNumber on queue to be sent")
            it.forEach {
                localQueueService.writeItemToQueue("send_message_queue", it) //messages added to queue one at a time for safety
            }
        }
    }


    /**
     * First step in the message compiling pipeline - change messages to match user characteristics to improve chances
     * the message suggestions will be implemented.
     *
     * @param initialResponse - an initial list of responses with what they correspond to
     * @param user - the user we are tailoring for
     * @return the responses tailored to user characteristics
     * */
    fun improveSuggestability(initialResponse: Map<HandlableMessageType, List<String>>, user : User) : Map<HandlableMessageType, List<String>>{
        logger().info("Improving suggestibility of message")
        initialResponse[HandlableMessageType.AGRICULTURAL_QUESTION]?.let{
            it.map {
                var knowledgeLevel = 0.0
                messageTaggingService.tagMessage(it, user.id)?.let {
                    user.knowledgeAreas.find { uka ->  (uka.crop == it.cropName) && (uka.topic == it.topic) }?.knowledgeLevel?.let{
                        knowledgeLevel =  it
                    }
                }

                compiler.userKnowledgeCompiling(it, knowledgeLevel)
            }
        }

        return initialResponse.mapValues{
            it.value.joinToString(" ").let {
                listOfNotNull(compiler.userCharacteristicCompiling(it, user.literacy ?: 50f,  user.gender ?: Gender.MALE, user.age ?: Age.ADULT,))
            }
        }.toSortedMap().also {
            logger().info("Message with better suggestibility is $it")
        }
    }

    /**
     * Splits/Amalgamates messages from different topics (general, agricultural_information, information_collection).
     * So user doesn't get very short or very long messages.
     *
     * Each topic is considered separately, and then all messages in each topic are amalgamated.
     * If this is less than 17 words, the now amalgamated message is sent.
     *
     * If the message is more than 17 words, it is split at punctuation using positive lookahead assertion.
     * Each sentence is considered individually, if the sentence has more than 17 words, that is sent by itself as a message.
     * If the message is less, it's added to a 'wait' list.
     * When the next sentence comes along if the wait list isn't empty, we try to add the two sentences together, and if
     * those are less than 17 we continue, else we remove the existing message in wait and send that as a message and if
     * the new one is less than 17, we add that to wait, else we send that as well.
     *
     * This is repeated until all messages are amalgamated or split.
     *
     * @param tailoredResponses - the responses tailored to user characteristics
     * @return a list of messages to be sent to the user, chunked and amalgamated as appropriate
     * */
    fun finalSplit(tailoredResponses: Map<HandlableMessageType, List<String>>) : List<String>{
        logger().info("Amalgamating message")
        val mappedTopics =  tailoredResponses.map { topic ->
            topic.value.joinToString(" ").let {
                if(it.split(" ").size > 17){
                    val result = mutableListOf<String>()
                    val wait = mutableListOf<String>()
                    it.split(Regex("(?<=[.!?])\\s*")).forEach {
                        if (wait.size == 0){
                            if (it.split(" ").size > 17){
                                result.add(it)
                            } else {
                                wait.add(it)
                            }
                        } else {
                            if (wait.joinToString(" ").split(" ").size + it.split(" ").size > 17){
                                result.add(wait[0])
                                wait.clear()
                                if (it.split(" ").size > 17){
                                    result.add(it)
                                } else {
                                    wait.add(it)
                                }
                            } else {
                                wait[0] = wait[0] + " " + it
                            }
                        }
                    }
                    result
                } else {
                    it
                }
            }
        }

        val toReturn = mutableListOf<String>()

        mappedTopics.forEach {
            if (it is String) toReturn.add(it)
            else if (it is List<*>){
                it.forEach {
                    toReturn.add(it as String)
                }
            }
        }

        logger().info("Amalgamated message is $toReturn")

        return toReturn
    }
}