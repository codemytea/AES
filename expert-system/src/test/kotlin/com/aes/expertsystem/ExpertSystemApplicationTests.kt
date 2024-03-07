package com.aes.expertsystem

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.expertsystem.Trefle.Models.PlantListDTO
import com.aes.expertsystem.Trefle.Controller
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
    lateinit var controller: Controller

    @Test
    fun getPlantByDTO() {
        controller.getPlantBySearchQuery(
            PlantListDTO(
                page = 1,
                q = "maize",
                filterNot = FilterNotDTO(
                    edible_part = null
                )
            )
        ).let {
            logger().info("${it.data[0].id?.let { it2 -> controller.getPlantById(Id(it2))}?.data?.main_species?.growth}")
        }
    }

    @Test
    fun getPlantList() {
        controller.allPlants(
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
        controller.getPlantById(Id(1))
    }

}
