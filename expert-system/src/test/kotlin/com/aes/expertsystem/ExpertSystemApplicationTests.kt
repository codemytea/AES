package com.aes.expertsystem

import com.aes.common.logging.Logging
import com.aes.expertsystem.Trefle.Models.PlantListDTO
import com.aes.expertsystem.Trefle.Controller
import com.aes.expertsystem.Trefle.Models.FilterNotDTO
import com.aes.expertsystem.Trefle.Models.Id
import com.aes.expertsystem.Trefle.Models.Responses.PlantIdResponseDTO.Growth
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@SpringBootTest
@ActiveProfiles("test")
class ExpertSystemApplicationTests : Logging {

    @Autowired
    lateinit var controller: Controller


    final inline fun <reified T> defaultNoArgConstructor(): T{
        return T::class.constructors.first { it.parameters.isEmpty() }.call()
    }


    final inline fun <reified T> List<T>.coalesce(): T{
        val newObj = defaultNoArgConstructor<T>()!!
        newObj::class.memberProperties.mapNotNull { it as? KMutableProperty<*> }.forEach {kProp->
            val firstNotNullOrNull = this.firstNotNullOfOrNull { kProp.getter.call(it) }
            kProp.setter.call(newObj, firstNotNullOrNull)
        }
        return newObj
    }

    @Test
    fun getPlantByDTO(){
        val r = getPlantByDTO_body()
        println(jacksonObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(r))
    }
    fun getPlantByDTO_body(): Growth {
        val data = controller.getPlantBySearchQuery(
            PlantListDTO(
                page = 1,
                q = "potatoes",
            )
        ).data.mapNotNull { //first 10
                it.id?.let {
                    controller.getPlantById(Id(it))
                }
            }
        val growthData = data.mapNotNull{it.data.main_species?.growth}
        return growthData.coalesce()
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
