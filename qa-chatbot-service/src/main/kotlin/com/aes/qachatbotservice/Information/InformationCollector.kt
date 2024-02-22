package com.aes.qachatbotservice.Information

import com.aes.common.Enums.UserDetails
import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.UserSmallholdingRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import java.util.*

class InformationCollector(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository
) : Logging {

    /**
     * Checks if there are any more details to determine about the user before responding
     * @return details that need to be collected
     * */
    fun moreDetailsToDetermine(message : String, userID : UUID) : List<UserDetails>{
        logger().info("Determining details of user with id $userID")
        //location -city, country (need to account for misspellings and local dialects!)
        //name
        //age
        //gender
        //smallholding size
        //commercial or sufficiency
        //cash crop/main crop

        return emptyList()
    }
}