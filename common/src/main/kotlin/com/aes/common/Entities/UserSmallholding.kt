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
    var location_city: String? = null,

    @Column
    var location_country: String? = null,

    @Column
    var smallholdingSize: Float? = null,

    @Column
    var isCommercial: Boolean? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    var cashCrop: Crop? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val soilType: SoilType? = null,

    )


