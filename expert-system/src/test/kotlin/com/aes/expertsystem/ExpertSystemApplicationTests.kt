package com.aes.expertsystem

import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import com.aes.common.Enums.Crop
import com.aes.common.Enums.SoilType
import com.aes.common.Weather.provider.WeatherService
import com.aes.common.logging.Logging
import com.aes.expertsystem.Data.Buying.services.SeedDataSavingService
import com.aes.expertsystem.Data.Buying.services.SeedPriceService
import com.aes.expertsystem.Data.CropGroup.services.CropGroupFetchService
import com.aes.expertsystem.Data.CropGroup.services.CropGroupParseService
import com.aes.expertsystem.Data.Selling.services.CropPriceSavingService
import com.aes.expertsystem.Data.Selling.services.CropSellingService
import com.aes.expertsystem.Data.Trefle.Models.FilterNotDTO
import com.aes.expertsystem.Data.Trefle.Models.Id
import com.aes.expertsystem.Data.Trefle.Models.PlantListDTO
import com.aes.expertsystem.Data.Trefle.Models.Responses.PlantIdResponseDTO.Growth
import com.aes.expertsystem.Data.Trefle.TrefleService
import com.aes.expertsystem.Data.services.SeedRateService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@SpringBootTest
@ActiveProfiles("test")
class ExpertSystemApplicationTests : Logging {
    @Autowired
    lateinit var trefleService: TrefleService

    @Autowired
    lateinit var ws: WeatherService

    @Autowired
    lateinit var spp: SeedPriceService

    @Autowired
    lateinit var cropPriceSavingService: CropPriceSavingService

    @Autowired
    lateinit var cropSellingService: CropSellingService

    @Autowired
    lateinit var seedDataSavingService: SeedDataSavingService

    @Autowired
    lateinit var ecocropDataSaveService: com.aes.expertsystem.Data.Ecocrop.Services.EcocropDataSaveService

    @Autowired
    lateinit var cropGroupParseService: CropGroupParseService

    @Autowired
    lateinit var cropGroupFetchService: CropGroupFetchService

    @Autowired
    lateinit var seedPriceService: SeedPriceService

    @Autowired
    lateinit var srs: SeedRateService

    final inline fun <reified T> defaultNoArgConstructor(): T {
        return T::class.constructors.first { it.parameters.isEmpty() }.call()
    }

    final inline fun <reified T> List<T>.coalesce(): T {
        val newObj = defaultNoArgConstructor<T>()!!
        newObj::class.memberProperties.mapNotNull { it as? KMutableProperty<*> }.forEach { kProp ->
            val firstNotNullOrNull = this.firstNotNullOfOrNull { kProp.getter.call(it) }
            kProp.setter.call(newObj, firstNotNullOrNull)
        }
        return newObj
    }

    @Test
    fun getPlantByDTO() {
        val r = getPlantByDTO_body()
        println(jacksonObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(r))
    }

    fun getPlantByDTO_body(): Growth {
        val data =
            trefleService.getPlantBySearchQuery(
                PlantListDTO(
                    page = 1,
                    q = "wheat",
                ),
            ).data.mapNotNull { // first 10
                it.id?.let {
                    trefleService.getPlantById(Id(it))
                }
            }
        val growthData = data.mapNotNull { it.data.main_species?.growth }
        return growthData.coalesce()
    }

    @Test
    fun getPlantList() {
        trefleService.allPlants(
            PlantListDTO(
                page = 1,
                filterNot =
                    FilterNotDTO(
                        edible_part = null,
                    ),
            ),
        )
    }

    @Test
    fun getPlantById() {
        trefleService.getPlantById(Id(1))
    }

    @Test
    fun saveSellingData() {
        cropPriceSavingService.writeAllToDB()
    }

    @Test
    fun getCropPriceWorks() {
        println(
            cropSellingService.getExpectedPriceForDateInCountry(
                "wheat",
                LocalDate.now().plusMonths(12),
                "United Kingdom",
            ),
        )
    }

    @Test
    fun useSavedSellingData() {
        val estimates =
            (0..24).forEach {
                val date = LocalDate.now().withDayOfMonth(1).plusMonths(it.toLong())
                val estimate =
                    cropSellingService.getExpectedPriceForDateInCountry(
                        "Wheat",
                        date,
                        "United Kingdom of Great Britain and Northern Ireland",
                    )
                println(date.format(DateTimeFormatter.ISO_DATE) + "," + estimate.toString())
            }
    }

    @Test
    fun saveSeedData() {
        seedDataSavingService.writeAllToDB()
    }

    @Test
    fun saveEcocropData() {
        ecocropDataSaveService.writeAllToDB()
    }

    @Test
    @Transactional
    fun seedRateTest() {
        val a =
            srs.seedRateForUserSmallholding(
                UserSmallholding(
                    user = User(),
                    location_city = "Coventry",
                    location_country = "United Kingdom",
                    smallholdingSize = 1.0f,
                    cashCrop = Crop.WHEAT,
                    soilType = SoilType.SILT,
                ),
                1000,
                LocalDateTime.now(),
            )
        println(a)
    }

    @Test
    fun saveCropgroupData() {
        cropGroupParseService.writeAllToDB()
    }

    @Test
    fun getCropDataWorks() {
        val mainGroup = cropGroupFetchService.getAllMainGroupsByName("sugar beet").minBy { it.groupNumber }
        println(mainGroup.groupNumber.toString() + " " + mainGroup.name)
        val subGroup = cropGroupFetchService.getAllSubGroupsByName("sugar beet").minBy { it.groupNumber }
        println(subGroup.groupNumber.toString() + subGroup.subgroupLetter + " " + subGroup.name)
    }

    @Test
    fun gettingSeedPriceForCountryWorks() {
        val price = seedPriceService.getSeedPriceForCropInCountryOnDate("Wheat", "United Kingdom", LocalDate.now())
        println("Price is ${price.first} per ${price.second.name}")
    }

    @Test
    fun sppTest() {
        println(spp.getSeedPriceForCropInCountryOnDate("wheat", "united kingdom", LocalDate.now()))
    }

    @Test
    fun wsTest() {
        println(
            ws.allFutureDatesMatchCondition(
                LocalDate.now(),
                LocalDate.now().plusMonths(9),
                "Coventry",
                "United Kingdom",
            ) {
                (it.temperature ?: 0f) > 9f
            },
        )
    }
}
