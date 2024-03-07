package com.aes.expertsystem.Models

import kotlin.reflect.full.memberProperties

class PlantDiseasesDTO (

    /**
     * ID of disease species
     * */
    val id : Int?,

    /**
     * The number page you want to see.
     * */
    val page : Int?,

    /**
     * A string/query consisting of keywords that are used to search for names of species
     * */
    val q : String?,
) {
    fun toQueryParams(): Map<String, Any>{
        return this::class.memberProperties.mapNotNull {
            if(it.getter.call(this) == null) null
            else{
                it.name to it.getter.call(this)!!
            }
        }.toMap()
    }
}
