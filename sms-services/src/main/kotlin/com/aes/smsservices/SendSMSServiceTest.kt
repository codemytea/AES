package com.aes.smsservices

import com.aes.common.Repositories.UserRepository
import com.aes.smsservices.Services.SendSmsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

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