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
import jakarta.transaction.Transactional
import org.apache.catalina.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class InformationCollector(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository,
    val informationCollection: InformationCollection,
) : Logging {

    private var stop = false

    /**
     * Checks if there are any more details left to determine about the user
     * @param userID - the ID of the user the system is checking
     * @return details that still need to be collected
     * */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDetailsToDetermine(userID: UUID): List<UserDetails>? {

        logger().info("")

        val knownSet = setOf<UserDetails>()

        val fullSet = setOf(*UserDetails.entries.toTypedArray())

        val user = userRepository.findUserById(userID)

        logger().info("Determining details of user with id $userID")
        user?.let {
            it.name?.let {
                knownSet.plus(UserDetails.NAME)
            }
        }

        //TODO check which smallholding the user is talking about and select the right one
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
    fun getNewInfo(message: MessageDTO, detailsToDetermine : List<UserDetails>?): Pair<List<UserDetails>?, String?>? {
        detailsToDetermine?.let { it ->
            //use NER to scrape any NEW information given by the received message
            val newInfo = informationCollection.getNewInformation(message.content, it)
            val newInfoCollected = mutableListOf<UserDetails>()

            val user = userRepository.findUserById(message.userID)
            val userSmallholding =
                user?.userSmallholdingInfo?.getOrNull(0) //TODO check which smallholding the user is talking about and select the right one

            //save info
            newInfo.forEach { (userDetail, info) ->
                val ud = userDetail.toUserDetails()
                if (info != null && ud != null) {
                    if (ud == UserDetails.NAME) {
                        newInfoCollected.add(UserDetails.NAME)
                        user?.name = info as String
                    } else if (ud == UserDetails.MAIN_CROP) {
                        newInfoCollected.add(UserDetails.MAIN_CROP)
                        userSmallholding?.cashCrop = (info as String).toCrop()
                    } else if (ud == UserDetails.LOCATION_CITY) {
                        newInfoCollected.add(UserDetails.LOCATION_CITY)
                        userSmallholding?.location_city = info as String
                    } else if (ud == UserDetails.LOCATION_COUNTRY) {
                        newInfoCollected.add(UserDetails.LOCATION_COUNTRY)
                        userSmallholding?.location_country = info as String
                    } else if (ud == UserDetails.SMALLHOLDING_SIZE) {
                        newInfoCollected.add(UserDetails.SMALLHOLDING_SIZE)
                        userSmallholding?.smallholdingSize = (info as String).toFloat()
                    }
                }
            }

            //TODO Integrate with NER?!
            newInfo["stopCollecting"]?.let { sc ->
                if (sc as Boolean) {
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

            // TODO integrate with NER
            //new details collected
            return Pair(newInfoCollected, newInfo["messageWithoutInformation"] as String)
        }

        return null
    }


    /**
     * Get any new information provided in user message, save to DB, and, if user hasn't asked to stop information
     * collection, compile message asking for any remaining information.
     *
     * @param message - the message without agricultural questions in it
     * @return Pair("call to action collection message asking for more information", "leftover original message")
     * */
    @Transactional
    fun askFormoreInfo(message: MessageDTO): Pair<String?, String?> {
        val detailsToDetermine = getDetailsToDetermine(message.userID)
        val newInfoAndRemainderMessage = getNewInfo(message, detailsToDetermine)
        logger().info("User details of user with id ${message.userID} has this new informtion ${newInfoAndRemainderMessage.toString()}")

        val userChoice = userRepository.findUserById(message.userID)?.stopCollectingInformation

        var callToAction: String? = null

        //get remaining details to determine
        detailsToDetermine?.minus(newInfoAndRemainderMessage?.first)?.let {
            if (!stop && userChoice != true) {
                logger().info("Asking user with id ${message.userID} for following new information: $it")
                callToAction = informationCollection.collect(it as List<UserDetails>)
                logger().info("Asking user with id ${message.userID} for following new information: $it with call to action: $callToAction")
            } else {
                logger().info("User with id ${message.userID} has asked for stop in info collection")
            }
        }

        return Pair(callToAction, newInfoAndRemainderMessage?.second)
    }
}