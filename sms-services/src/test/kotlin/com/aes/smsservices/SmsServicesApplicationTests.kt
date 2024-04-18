package com.aes.smsservices

import com.aes.common.Buying.services.SeedDataSavingService
import com.aes.common.Buying.services.SeedPriceService
import com.aes.common.CropGroup.services.CropGroupFetchService
import com.aes.common.CropGroup.services.CropGroupParseService
import com.aes.common.Ecocrop.Services.EcocropDataSaveService
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.Crop
import com.aes.common.Enums.MessageType
import com.aes.common.Enums.SoilType
import com.aes.common.Models.NewMessageDTO
import com.aes.common.Models.RecipientDTO
import com.aes.common.Selling.services.CropPriceSavingService
import com.aes.common.Selling.services.CropSellingService
import com.aes.common.Sowing.services.SeedRateService
import com.aes.common.Weather.provider.WeatherService
import com.aes.smsservices.Services.SendSmsService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class SmsServicesApplicationTests {
    @Autowired
    lateinit var sendSmsService: SendSmsService

    @Autowired
    lateinit var ws : WeatherService

    @Autowired
    lateinit var spp : SeedPriceService

    @Autowired
    lateinit var cropPriceSavingService: CropPriceSavingService

    @Autowired
    lateinit var cropSellingService: CropSellingService

    @Autowired
    lateinit var seedDataSavingService: SeedDataSavingService

    @Autowired
    lateinit var ecocropDataSaveService: EcocropDataSaveService

    @Autowired
    lateinit var cropGroupParseService: CropGroupParseService

    @Autowired
    lateinit var cropGroupFetchService: CropGroupFetchService

    @Autowired
    lateinit var seedPriceService: SeedPriceService

    @Autowired
    lateinit var srs : SeedRateService

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
    fun getCropPriceWorks(){
        println(cropSellingService.getExpectedPriceForDateInCountry("wheat", LocalDate.now().plusMonths(12), "United Kingdom"))
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

    @Test
    fun saveEcocropData(){
        ecocropDataSaveService.writeAllToDB()
    }

    @Test
    @Transactional
    fun seedRateTest(){
        val a = srs.seedRateForUserSmallholding(
            UserSmallholding(
            userId = UUID.randomUUID(),
            location_city = "Coventry",
            location_country = "United Kingdom",
            smallholdingSize = 1.0f,
            cashCrop = Crop.WHEAT,
            soilType = SoilType.SILT,
        ), 1000, LocalDateTime.now())
        println(a)
    }

    @Test
    fun saveCropgroupData(){
        cropGroupParseService.writeAllToDB()
    }

    @Test
    fun getCropDataWorks(){
        val mainGroup = cropGroupFetchService.getAllMainGroupsByName("sugar beet").minBy { it.groupNumber }
        println(mainGroup.groupNumber.toString() + " " + mainGroup.name)
        val subGroup = cropGroupFetchService.getAllSubGroupsByName("sugar beet").minBy { it.groupNumber }
        println(subGroup.groupNumber.toString() + subGroup.subgroupLetter + " " + subGroup.name)
    }

    @Test
    fun gettingSeedPriceForCountryWorks(){
        val price = seedPriceService.getSeedPriceForCropInCountryOnDate("Wheat", "United Kingdom", LocalDate.now())
        println("Price is ${price.first} per ${price.second.name}")
    }

    @Test
    fun sppTest(){
        println(spp.getSeedPriceForCropInCountryOnDate("wheat", "united kingdom", LocalDate.now()))
    }

    @Test
    fun wsTest(){
        println(ws.allFutureDatesMatchCondition(LocalDate.now(), LocalDate.now().plusMonths(9), "Coventry", "United Kingdom"){
            (it.temperature?:0f) > 9f
        })
    }

}

