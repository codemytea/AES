package com.aes.qachatbotservice.Information

import com.aes.common.Repositories.UserRepository
import com.aes.common.Repositories.UserSmallholdingRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import java.util.*

class InformationCollector(
    val userRepository: UserRepository,
    val userSmallholdingRepository: UserSmallholdingRepository
) : Logging {

    fun determineDetails(message : String, userID : UUID){
        logger().info("Determining details of user with id $userID")
        //location -city, country (needto account for misspellings and local dialects!)
        //name
        //age
        //gender
        //smallholding size
        //commercial or sufficiency
        //cash crop/main crop



    }
}