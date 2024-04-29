package com.aes.messagehandler.Services.Detectors

import com.aes.common.Enums.HandlableMessageType
import com.aes.common.Enums.UserDetails
import com.aes.common.Repositories.UserRepository
import com.aes.messagehandler.Interfaces.MessageHandler
import com.aes.messagehandler.Python.ReferAFriendExtraction
import com.aes.messagehandler.Services.NewInformationService
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*

@Order(3)
@Service
class ReferAFriendDetector(
    private val referAFriendExtraction: ReferAFriendExtraction,
    private val newInformationService: NewInformationService,
    private val userRepository: UserRepository,
) : MessageHandler {
    override val messagePartType: HandlableMessageType = HandlableMessageType.REFER_FRIEND
    var containsPhoneNumber = false

    val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    override fun extractPartAndReturn(
        remainingMessage: String,
        userID: UUID,
    ): List<String>? {
        val info = referAFriendExtraction.getReferral(remainingMessage).mapNotNull { it }.ifEmpty { null }
        info?.let { infoInner ->
            val numbers = phoneNumberUtil.findNumbers(infoInner.joinToString(" "), "GB")?.toList()
            if (numbers?.isNotEmpty() == true) {
                if (userRepository.findByPhoneNumberContaining(
                        (numbers.first().number().countryCode.toString() + numbers.first().number().nationalNumber.toString()).toLong(),
                    ) == null
                ) {
                    containsPhoneNumber = true
                    newInformationService.saveNewInformation(
                        infoInner.joinToString(" "),
                        userID,
                        listOf(
                            UserDetails.NAME,
                            UserDetails.MAIN_CROP,
                            UserDetails.LOCATION_CITY,
                            UserDetails.LOCATION_COUNTRY,
                            UserDetails.SMALLHOLDING_SIZE,
                        ),
                        (numbers.first().number().countryCode.toString() + numbers.first().number().nationalNumber.toString()).toLong(),
                    )
                }
            }
        }

        return info
    }

    override fun generateAnswer(
        prompts: List<String>,
        userID: UUID,
    ): List<String>? {
        return if (containsPhoneNumber) {
            listOf("Thank you for your referral.")
        } else {
            listOf("Please repeat your referral, but include the users phone number as well, or check the number is in the correct format.")
        }
    }
}
