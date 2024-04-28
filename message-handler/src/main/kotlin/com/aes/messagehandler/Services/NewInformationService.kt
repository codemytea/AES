package com.aes.messagehandler.Services

import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.UserDetails
import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.UserSmallholdingRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagehandler.Mappers.toCrop
import com.aes.messagehandler.Mappers.toHectares
import com.aes.messagehandler.Python.InformationCollection
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewInformationService(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository,
    val informationCollection: InformationCollection,
) : Logging {

    /**
     * Checks if there are any more details left to determine about the user
     * @param userID - the ID of the user the system is checking
     * @return details that still need to be collected
     * */
    @OptIn(ExperimentalStdlibApi::class)
    fun getDetailsToDetermine(userID: UUID): List<UserDetails>? {
        val knownSet = setOf<UserDetails>()
        val fullSet = setOf(*UserDetails.entries.toTypedArray())
        val user = userRepository.findUserById(userID)

        logger().info("Determining details of user with id $userID")
        user?.let {
            it.name?.let {
                knownSet.plus(UserDetails.NAME)
            }
        }

        user?.userSmallholdingInfo?.getOrNull(0)?.let {
            it.location_city?.let {
                knownSet.plus(UserDetails.LOCATION_CITY)
            }

            it.location_country?.let {
                knownSet.plus(UserDetails.LOCATION_COUNTRY)
            }

            it.cashCrop?.let {
                knownSet.plus(UserDetails.MAIN_CROP)
            }

            it.smallholdingSize?.let {
                knownSet.plus(UserDetails.SMALLHOLDING_SIZE)
            }
        }

        val returnList = mutableListOf<UserDetails>()

        fullSet.minus(knownSet).forEach {
            returnList.add(it)
        }

        return if (returnList.isEmpty()) null else returnList
    }


    /**
     * Using the user's message, and what information there is left to determine about the user
     * Check if the message contains any new information and return a list of any new user information obtained
     * as well as processing the message and removing parts that give that new information, and checking whether or not
     * the user has asked for information collection to stop.
     *
     * @param message - the message sent by the user
     * @param detailsToDetermine - information the system doesn't know about the user
     * @return a pair of new information collected about the user, and the message without the bits that give that new information
     * */
    @Transactional
    fun saveNewInformation(
        message: String,
        userID: UUID,
        detailsToDetermine: List<UserDetails>?,
        newUserNumber: Long? = null
    ) {
        detailsToDetermine?.let { it ->
            //use NER to scrape any NEW information given by the received message
            val newInfo = informationCollection.getNewInformation(message, it)
            val newInfoCollected = mutableListOf<UserDetails>()

            var user: User?

            if (newUserNumber == null) {
                user = userRepository.findUserById(userID)

            } else {
                user = userRepository.save(
                    User(
                        UUID.randomUUID(),
                        listOf(newUserNumber)
                    )
                )
            }

            var userSmallholding = user?.userSmallholdingInfo?.getOrNull(0)

            if (userSmallholding == null) {
                userSmallholding = userSmallholdingRepository.save(
                    UserSmallholding(
                        user = user
                    )
                )
            }

            //save info
            newInfo.forEach { (userDetail, info) ->
                if (info != null){
                    when (userDetail) {
                        UserDetails.NAME.strForm -> {
                            newInfoCollected.add(UserDetails.NAME)
                            user?.name = info
                        }
                        UserDetails.MAIN_CROP.strForm -> {
                            newInfoCollected.add(UserDetails.MAIN_CROP)
                            userSmallholding.cashCrop = info.toCrop()
                        }
                        UserDetails.LOCATION_CITY.strForm -> {
                            newInfoCollected.add(UserDetails.LOCATION_CITY)
                            userSmallholding.location_city = info
                        }
                        UserDetails.LOCATION_COUNTRY.strForm -> {
                            newInfoCollected.add(UserDetails.LOCATION_COUNTRY)
                            userSmallholding.location_country = info
                        }
                        UserDetails.SMALLHOLDING_SIZE.strForm -> {
                            newInfoCollected.add(UserDetails.SMALLHOLDING_SIZE)
                            userSmallholding.smallholdingSize = info.toHectares()
                        }
                    }
                }
            }

            user?.let {
                userRepository.save(it)
            }
            userSmallholding.let {
                userSmallholdingRepository.save(it)
            }

        }
    }
}