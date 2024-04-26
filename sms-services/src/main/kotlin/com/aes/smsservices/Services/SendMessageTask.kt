package com.aes.smsservices.Services

import com.aes.common.Models.NewMessageDTO
import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.findAllPhoneNumbersByMessagesIsEmpty
import com.aes.common.logging.Logging
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Configuration
@EnableScheduling
class SendMessageTask(
    private val sendSmsService: SendSmsService,
    private val userRepository: UserRepository
) : Logging {
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    fun sendNewSmallholdersMessage() {
        userRepository.findAllPhoneNumbersByMessagesIsEmpty()?.let {
            if (it.isNotEmpty()){
                sendSmsService.sendSMS(
                    NewMessageDTO(
                        message = "Hello, you have been referred to this service by a friend. You can ask me any agricultural queries you have.",
                        recipients = it
                    )
                )
            }

        }
    }
}
