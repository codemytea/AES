package com.aes.notificationservice.event

import com.aes.common.Enums.Crop
import com.aes.common.Enums.Topic
import com.aes.notificationservice.location.Location
import java.time.LocalDateTime

fun interface ExecutionType {


    fun filterCropsAndTopics(crops: List<Crop>, topic: List<Topic>): Map<Crop, Topic>


    companion object{
        val ALL = ExecutionType{ crops, topics ->
            crops.flatMap { c->
                topics.map {t->
                    c to t
                }
            }.toMap()
        }
        val ANY = ExecutionType{crops, topics->
            if(crops.isNotEmpty() && topics.isNotEmpty()){
                mapOf(crops.first() to topics.first())
            } else mapOf()
        }

        fun CROP_FLAG_DRIVEN(getFlag: (Crop)->Boolean) = ExecutionType{crops, topics->
            topics.flatMap {t->
                crops.filter(getFlag).map {c->
                    c to t
                }
            }.toMap()
        }
    }

}