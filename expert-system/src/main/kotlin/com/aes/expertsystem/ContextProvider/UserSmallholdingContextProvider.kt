package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import org.springframework.stereotype.Component

@Component
class UserSmallholdingContextProvider : InitialContextProvider {
    override fun contextForMessage(
        message: String,
        user: User,
    ): List<String> {
        return user.userSmallholdingInfo.flatMap {
            listOfNotNull(
                it.location_country?.let { "in the country $it" },
                it.location_city?.let { "in the city $it" },
                it.smallholdingSize?.let { "$it hectares" },
                it.cashCrop?.name?.lowercase()?.let { "used to grow $it" },
            ).map {
                "The user's smallholding is $it."
            }
        } + listOf("The user's smallholding information should be used to scale any results.")
    }
}
