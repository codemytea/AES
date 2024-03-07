package com.aes.expertsystem.Models

import com.aes.expertsystem.Enums.Cycle
import com.aes.expertsystem.Enums.Order
import com.aes.expertsystem.Enums.Sunlight
import com.aes.expertsystem.Enums.Watering
import kotlin.reflect.full.memberProperties

class PlantListDTO(

    /**
     * The number page you want to see.
     * */
    val page : Int? = null,

    /**
     * A string/query consisting of keywords that are used to search for names of species
     * */
    val q : String? = null,

    /**
     * Alphabetical order species common name
     * */
    val order : Order? = null,

    /**
     * If plant species is edible or not for consumption
     * */
    val edible : Boolean? = null,

    /**
     * If plant species is poisonous or not
     * */
    val poisonous : Boolean? = null,

    /**
     * The plant cycle of the species.
     * */
    val cycle : Cycle? = null,

    /**
     * The watering amount of the species.
     * */
    val watering : Watering? = null,

    /**
     * The sunlight amount of the species.
     * */
    val sunlight : Sunlight? = null,

    /**
     * If plant species is indoors
     * */
    val indoor : Boolean? = null,

    /**
     * Hardiness Zone of plant species
     * */
    val hardiness : Int? = null,
){

    fun toQueryParams(): Map<String, Any>{
        return this::class.memberProperties.mapNotNull {
            if(it.getter.call(this) == null) null
            else{
                it.name to it.getter.call(this)!!
            }
        }.toMap()
    }
}