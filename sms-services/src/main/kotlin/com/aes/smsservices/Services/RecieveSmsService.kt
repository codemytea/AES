package com.aes.smsservices.Services

import com.aes.common.Entities.Message
import com.aes.common.Entities.User
import com.aes.common.Enums.LanguageCode
import com.aes.common.Enums.MessageType
import com.aes.common.Models.MessageQueueItem
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.smsservices.Mappers.getLanguageCodeForCountry
import com.aes.smsservices.Mappers.toStandardLanguage
import com.aes.smsservices.Models.RecievedMessageDTO
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RecieveSmsService(
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val translateSmsService: TranslateSmsService,
    val localQueueService: LocalQueueService
) : Logging {

    fun sendToMessageHandler(sms: Message) {
        val messageQueueItem = MessageQueueItem(sms.id)
        localQueueService.writeItemToQueue("message_handler_queue", messageQueueItem)
    }

    /**
     * Saves incoming message to DB, in english
     *
     * @param resource - the received message
     * @return the message entity
     * */
    @Transactional
    fun save(resource: RecievedMessageDTO): Message {

        //find user to associate message with or create a new user
        val user = userRepository.findByPhoneNumberContaining(resource.phoneNumber) ?: let {
            userRepository.save(
                User(
                    id = UUID.randomUUID(),
                    phoneNumber = listOf(resource.phoneNumber),
                    //if it's a new user, figure out their language from their country code
                    preferredLanguage = LanguageCode.fromLanguage(getLanguageCodeForCountry(resource.country).toStandardLanguage())
                        ?: LanguageCode.EN,
                    stopCollectingInformation = false
                )
            )
        }

        val lang = user.preferredLanguage ?: LanguageCode.EN

        //if the incoming message isn't in english, translate it into english
        if (lang != LanguageCode.EN) {
            resource.message = translateSmsService.translateMessage(resource.message, fromLanguage = lang)
        }

        //return the new message entity and save it to the DB
        return Message(
            resource.id,
            resource.phoneNumber,
            resource.message,
            LocalDateTime.now(),
            user,
            MessageType.INCOMING
        ).also { messageRepository.save(it) }
    }


}