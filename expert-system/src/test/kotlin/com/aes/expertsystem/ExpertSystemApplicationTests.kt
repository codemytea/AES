package com.aes.expertsystem

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.common.Trefle.Models.PlantListDTO
import com.aes.common.Trefle.TrefleService
import com.aes.expertsystem.Trefle.Models.FilterNotDTO
import com.aes.expertsystem.Trefle.Models.Id
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ExpertSystemApplicationTests : Logging {

    @Autowired
    lateinit var trefleService: TrefleService

    @Test
    fun getPlantByDTO() {
        trefleService.getPlantBySearchQuery(
            PlantListDTO(
                page = 1,
                q = "maize",
                filterNot = FilterNotDTO(
                    edible_part = null
                )
            )
        ).let {
            logger().info("${it.data[0].id?.let { it2 -> trefleService.getPlantById(Id(it2))}?.data?.main_species?.growth}")
        }
    }

    @Test
    fun getPlantList() {
        trefleService.allPlants(
            PlantListDTO(
                page = 1,
                filterNot = FilterNotDTO(
                    edible_part = null
                )
            )
        )
    }

    @Test
    fun getPlantById() {
        trefleService.getPlantById(Id(1))
    }

}
