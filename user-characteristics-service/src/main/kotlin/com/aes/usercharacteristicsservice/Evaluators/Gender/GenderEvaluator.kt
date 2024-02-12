package com.aes.usercharacteristicsservice.Evaluators.Gender;

import com.aes.common.Enums.Gender
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
@Configuration
@EnableScheduling
class GenderEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val attributeEstimator: AttributeEstimator
) : Logging {

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    //@Scheduled(cron = "0/10 * * ? * *")
    fun getGenderEstimate(){

        userRepository.findAll().forEach { user ->

            val messages = messageRepository.getMessageByUserIdAndType(user.id).also {
                if (it.isEmpty()) return@forEach
            }
            val gender = attributeEstimator.estimateGender(messages.map { it.message })

            logger().info("User ${user.id} estimated gender is $gender")

            user.gender = gender
            userRepository.save(user)
        }
    }
}
