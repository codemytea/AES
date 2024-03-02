package com.aes.messagecompiler.Controller

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.UserRepository
import com.aes.messagecompiler.Mappers.toNewMessageDTO
import com.aes.messagecompiler.Python.Compiler
import org.springframework.stereotype.Service

@Service
class CompilerPipeline(
    val compiler: Compiler,
    val userRepository: UserRepository,
    val localQueueService: LocalQueueService
) {

    /**
     * Compiles a message by tweaking it to the users characteristics and then chunking/amalgamating them to one message long chunks.
     * @return list of NewMessageDTOs ready to be sent
     * */
    fun compileMessage(userMessage : Map<String, List<String>>, phoneNumber : Long) : List<NewMessageDTO>? {
        val user = userRepository.findByPhoneNumberContaining(phoneNumber)
        val improved = improveSuggestability(userMessage, user?.literacy ?: 50f, user?.age ?: Age.ADULT, user?.gender ?: Gender.MALE) //if not known, assume standard

        return finalSplit(improved).toNewMessageDTO(phoneNumber).also {
            localQueueService.writeItemToQueue("send_message_queue", it)
        }
    }


    /**
     * First step in the message compiling pipeline - change messages to match user characteristics to improve chances the message suggestions will be implemented.
     * */
    fun improveSuggestability(userMessage: Map<String, List<String>>, literacyLevel : Float, age : Age, gender: Gender) : Map<String, List<String?>>{
        return userMessage.mapValues{
            it.value.joinToString(" ").let {
                listOf(compiler.compile(it, literacyLevel, gender, age))
            }
        }
    }

    /**
     * Splits/Amalgamates messages from different topics (general, agricultural_information, information_collection).
     * So user doesn't get very short or very long messages.
     *
     * Each topic is considered separately, and then all messages in each topic are amalgamated.
     * If this is less than 25 words, the now amalgamated message is sent.
     *
     * If the message is more than 25 words, it is split at punctuation using positive lookahead assertion.
     * Each sentence is considered individually, if the sentence has more than 25 words, that is sent by itself as a message.
     * If the message is less, it's added to a 'wait' list.
     * When the next sentence comes along if the wait list isn't empty, we try to add the two sentences together, and if those are less than 25 we continue,
     * else we remove teh existing message in wait and send that as a message and if the new one is less than 25, we add that to wait, else we send that as well.
     *
     * This is repeated until all messages are amalgamated or split.
     *
     * This to send messages are @return as a 1D list of messages
     * */
    fun finalSplit(userMessage: Map<String, List<String?>>) : List<String>{
        val mappedTopics =  userMessage.map {topic ->
            topic.value.joinToString(" ").let {
                if(it.split(" ").size > 25){
                    val result = mutableListOf<String>()
                    val wait = mutableListOf<String>()
                    it.split(Regex("(?<=[.!?])\\s*")).forEach {
                        if (wait.size == 0){
                            if (it.split(" ").size > 25){
                                result.add(it)
                            } else {
                                wait.add(it)
                            }
                        } else {
                            if (wait.joinToString(" ").split(" ").size + it.split(" ").size > 25){
                                result.add(wait[0])
                                wait.clear()
                                if (it.split(" ").size > 25){
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

        return toReturn
    }
}