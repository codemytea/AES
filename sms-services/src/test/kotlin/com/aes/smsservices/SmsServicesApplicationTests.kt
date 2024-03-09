package com.aes.smsservices

import com.aes.common.Buying.services.SeedDataSavingService
import com.aes.common.Enums.MessageType
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO
import com.aes.common.Selling.services.CropPriceSavingService
import com.aes.common.Selling.services.CropSellingService
import com.aes.smsservices.Services.SendSmsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest
@ActiveProfiles("test")
class SmsServicesApplicationTests {
    @Autowired
    lateinit var sendSmsService: SendSmsService

    @Autowired
    lateinit var cropPriceSavingService: CropPriceSavingService

    @Autowired
    lateinit var cropSellingService: CropSellingService

    @Autowired
    lateinit var seedDataSavingService: SeedDataSavingService

    @Test
    fun sendMessage() {
        sendSmsService.sendSMS(
            NewMessageDTO(
                message = "this is a test!",
                recipient = RecipientDTO(
                    447565533834
                )
            ),
            MessageType.OUTGOING
        )
    }

    @Test
    fun saveSellingData(){
        cropPriceSavingService.writeAllToDB()
    }

    @Test
    fun useSavedSellingData(){
        val estimates = (0..24).forEach{
            val date = LocalDate.now().withDayOfMonth(1).plusMonths(it.toLong())
            val estimate = cropSellingService.getExpectedPriceForDateInCountry(
                "Wheat",
                date,
                "United Kingdom of Great Britain and Northern Ireland"
            )
            println(date.format(DateTimeFormatter.ISO_DATE) + "," + estimate.toString())
        }
    }

    @Test
    fun saveSeedData(){
        seedDataSavingService.writeAllToDB()
    }

}

