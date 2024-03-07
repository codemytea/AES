package com.aes.expertsystem.Trefle.Models

import kotlin.reflect.full.memberProperties

class Id (
    val id : Int
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