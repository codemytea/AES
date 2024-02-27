package com.aes.messagehandler.Information

import com.aes.common.Enums.UserDetails
import com.aes.common.Models.MessageDTO
import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.UserSmallholdingRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagehandler.Mappers.toCrop
import com.aes.messagehandler.Mappers.toUserDetails
import com.aes.messagehandler.Python.InformationCollection
import org.springframework.stereotype.Service
import jakarta.transaction.Transactional
import java.util.*


@Service
class InformationCollector(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository,
    val informationCollection: InformationCollection,
) : Logging {

    private var stop = false

    /**
     * Checks if there are any more details to determine about the user before responding
     * @return details that need to be collected
     * */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDetailsToDetermine(userID: UUID, newInfo: List<UserDetails>? = null): List<UserDetails>? {

        val knownSet = setOf<UserDetails>()
        if (newInfo != null) {
            knownSet.plus(newInfo)
        }
        val fullSet = setOf(UserDetails.entries)

        val user = userRepository.findUserById(userID)

        logger().info("Determining details of user with id $userID")
        user?.let {
            it.name?.let {
                knownSet.plus(UserDetails.NAME)
            }
        }

        //TODO check which smallholding the user is talking about and select the right one
        user?.userSmallholdingInfo?.get(0)?.let {
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

            it.isCommercial?.let {
                knownSet.plus(UserDetails.IS_COMMERCIAL)
            }
        }

        val returnList = mutableListOf<UserDetails>()

        fullSet.minus(knownSet).forEach {
            returnList.add(it as UserDetails)
        }

        return if (returnList.isEmpty()) null else returnList
    }


    /**
     * Using the user's message, and what info there is left to determine
     * Try see if this message contains any new info
     * And return a list of the new user details that the user now has
     * */
    @Transactional
    fun getNewInfo(message: MessageDTO): Pair<List<UserDetails>?, String?>? {
        getDetailsToDetermine(message.userID)?.let {
            val newInfo = informationCollection.secondLine(message.content, it)

            val user = userRepository.findUserById(message.userID)
            val userSmallholding = user?.userSmallholdingInfo?.get(0) //TODO check which smallholding the user is talking about and select the right one

            //save info
            newInfo.forEach { (userDetail, info) ->
                var ud = userDetail.toUserDetails()
                if (info != null) {
                    if (ud == UserDetails.NAME) {
                        user?.name = info
                    } else if (ud == UserDetails.MAIN_CROP) {
                        userSmallholding?.cashCrop = info.toCrop()
                    } else if (ud == UserDetails.LOCATION_CITY) {
                        userSmallholding?.location_city = info
                    } else if (ud == UserDetails.LOCATION_COUNTRY) {
                        userSmallholding?.location_country = info
                    } else if (ud == UserDetails.SMALLHOLDING_SIZE) {
                        userSmallholding?.smallholdingSize = info.toFloat()
                    } else if (ud == UserDetails.IS_COMMERCIAL) {
                        userSmallholding?.isCommercial = info.toBoolean()
                    }
                }
            }

            newInfo["stopCollecting"]?.let {
                if (it.toBoolean()){
                    user?.stopCollectingInformation = true
                    stop = true
                }
            }

            user?.let {
                userRepository.save(it)
            }
            userSmallholding?.let {
                userSmallholdingRepository.save(it)
            }

            //new details collected
            return Pair(newInfo.map { it.key.toUserDetails()!! }, newInfo["messageWithoutInformation"])
        }

        return null
    }


    /**
     * 1. get info from message and return what new info garnered
     * 2. leftToFind =  all info - (pre-existing info + new info)
     * 3. ask user for left to find
     * @return Pair("call to action collection message", "leftover original message")
     * */
    @Transactional
    fun askFormoreInfo(message: MessageDTO) : Pair<String?, String?> {
        val newInfoAndRemainderMessage = getNewInfo(message)
        val userChoice = userRepository.findUserById(message.userID)?.stopCollectingInformation
        var callToAction: String? = null
        getDetailsToDetermine(message.userID, newInfoAndRemainderMessage?.first)?.let {
            if (!stop && userChoice != true){
                callToAction = informationCollection.collect(it)
            }

        }
        return Pair(callToAction, newInfoAndRemainderMessage?.second)
    }


    //extract message without info
}