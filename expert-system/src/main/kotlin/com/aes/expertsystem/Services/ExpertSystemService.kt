package com.aes.expertsystem.Services

import com.aes.common.Entities.User
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.ContextProvider.ContextProvider
import com.aes.expertsystem.ContextProvider.InitialContextProvider
import com.aes.expertsystem.Python.ExpertSystem
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpertSystemService(
    private val expertSystem: ExpertSystem,
    private val contextProviders: List<ContextProvider>,
    private val initialContextProviders: List<InitialContextProvider>,
) : Logging {
    @Transactional
    fun getAgriculturalAnswer(
        message: String,
        user: User,
    ): String {
        val initialContext =
            initialContextProviders.flatMap {
                it.contextForMessage(message, user)
            }

        logger().info("Getting initial answer")

        val initialAnswer = expertSystem.getAnswer(message, initialContext)

        logger().info("Initial answer was $initialAnswer")

        val newContextMessage = "$message $initialAnswer"

        val fullContext =
            contextProviders.flatMap {
                it.contextForMessage(newContextMessage, user)
            }

        logger().info("Getting full answer")

        return expertSystem.getAnswer(message, fullContext).also {
            println("Answer is $it")
        }
    }
}
