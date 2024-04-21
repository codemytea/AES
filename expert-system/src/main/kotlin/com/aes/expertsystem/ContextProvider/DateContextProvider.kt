package com.aes.expertsystem.ContextProvider

import com.aes.common.Entities.User
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class DateContextProvider: ContextProvider {
    override fun contextForMessage(message: String, user: User): List<String> {
        return listOf(
            "Today is ${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}",
        )
    }
}