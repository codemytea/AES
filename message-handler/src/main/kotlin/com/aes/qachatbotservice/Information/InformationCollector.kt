package com.aes.qachatbotservice.Information

import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.UserDetails
import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.UserSmallholdingRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.qachatbotservice.Python.InformationCollectionNER
import java.util.*

class InformationCollector(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository,
    val informationCollectionNER: InformationCollectionNER
) : Logging {

    /**
     * Checks if there are any more details to determine about the user before responding
     * @return details that need to be collected
     * */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDetailsToDetermine(userID : UUID, newInfo : List<UserDetails>? = null) : List<UserDetails>?{

        val knownSet = setOf<UserDetails>()
        if (newInfo != null){
            knownSet.plus(newInfo)
        }
        val fullSet = setOf(UserDetails.entries)

        logger().info("Determining details of user with id $userID")
        userRepository.findUserById(userID)?.let {
            it.name?.let {
                knownSet.plus(UserDetails.NAME)
            }
        }

        userSmallholdingRepository.findUserSmallholdingById(userID)?.let {
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

    fun getNewInfo(message : String, userID : UUID) : List<UserDetails>?{
        getDetailsToDetermine(userID)?.let{
            val newInfo = informationCollectionNER.collect(it, message)

            //save info

            //new details collected
            return newInfo.map{ it.key }
        }

        return null
    }


    fun askFormoreInfo(message:String, userID: UUID){
        getDetailsToDetermine(userID, getNewInfo(message, userID))
    }
}