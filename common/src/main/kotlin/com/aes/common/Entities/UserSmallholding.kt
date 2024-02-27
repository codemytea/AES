package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.SoilType
import jakarta.persistence.*
import java.io.Serializable
import java.util.*


data class UserSmallholdingPoint(
    val lat: Int,
    var lng: Int
) : Serializable

@Entity
class UserSmallholding(

    @Id
    val userId: UUID = UUID.randomUUID(),

    @Column
    val location_city: String? = null,

    @Column
    val location_country: String? = null,

    @Column
    val smallholdingSize: Float? = null,

    @Column
    val isCommercial: Boolean? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val cashCrop: Crop? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val soilType: SoilType? = null,

    )


