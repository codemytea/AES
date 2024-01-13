package com.aes.smsservices

import com.aes.smsservices.Entities.User
import com.aes.smsservices.Enums.LanguageCode
import com.aes.smsservices.Models.NewMessageDTO
import com.aes.smsservices.Models.RecipientDTO
import com.aes.smsservices.Repositories.UserRepository
import com.aes.smsservices.Services.SendSmsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class SendSMSServiceTest(
    val sendSMSService: SendSmsService,
    val userRepository: UserRepository
): ApplicationRunner{

    override fun run(args: ApplicationArguments?) {

//        val user = userRepository.findByPhoneNumberContaining(447565533834) ?: let{
//            userRepository.save(
//                User(
//                    UUID.randomUUID(),
//                    listOf(447565533834),
//                    LanguageCode.FR
//                )
//            )
//        }
//
//        sendSMSService.sendSMS(NewMessageDTO(
//            "New test",
//            recipient = RecipientDTO(
//                447565533834
//            )
//        ))


    }

}