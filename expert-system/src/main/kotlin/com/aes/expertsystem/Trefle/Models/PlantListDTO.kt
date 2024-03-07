package com.aes.expertsystem.Trefle.Models

import kotlin.reflect.full.memberProperties

class PlantListDTO(

    /**
     * The number page you want to see.
     * */
    val page: Int? = null,

    val filter: FilterDTO? = null,

    val filterNot: FilterNotDTO? = null,

    val order: OrderDTO? = null,

    val range: RangeDTO? = null,

    val q : String? = null
) {

    fun toQueryParams(): Map<String, Any> {
        return this::class.memberProperties.mapNotNull {
            if (it.getter.call(this) == null) null
            else {
                it.name to it.getter.call(this)!!
            }
        }.toMap()
    }
}