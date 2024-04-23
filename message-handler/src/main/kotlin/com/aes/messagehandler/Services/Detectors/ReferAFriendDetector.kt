package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Enums.UserDetails
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.ReferAFriendExtraction
import com.aes.messagehandler.Services.NewInformationService
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(4)
@Service
class ReferAFriendDetector(
    private val referAFriendExtraction: ReferAFriendExtraction,
    private val newInformationService: NewInformationService,
) : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.REFER_FRIEND
    var containsPhoneNumber = false

    val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    override fun extractPartAndReturnRemaining(remainingMessage: String, userID : UUID): List<String>? {

        val info = referAFriendExtraction.getReferral(remainingMessage).mapNotNull { it }.ifEmpty { null }
        info?.let { infoInner->
            val numbers = phoneNumberUtil.findNumbers(infoInner.joinToString(" "), "GB")
            if (numbers != null){
                containsPhoneNumber = true
                newInformationService.saveNewInformation(
                    infoInner.joinToString(" "),
                    userID,
                    listOf(
                        UserDetails.NAME, UserDetails.MAIN_CROP,
                        UserDetails.LOCATION_CITY, UserDetails.LOCATION_COUNTRY,
                        UserDetails.SMALLHOLDING_SIZE
                    ),
                    numbers.first().number().nationalNumber
                )
            }
        }

        return info

    }

    override fun generateAnswer(prompts : List<String>, userID : UUID): List<String>? {
        return if(containsPhoneNumber){
            listOf("Thank you for your referral.")
        } else {
            listOf("Please repeat your referral, but include the users phone number as well.")
        }
    }
}