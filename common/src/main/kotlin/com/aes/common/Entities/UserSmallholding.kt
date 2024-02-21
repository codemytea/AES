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
@IdClass(UserSmallholdingId::class)
class UserSmallholding(

    @Id
    val userId: UUID = UUID.randomUUID(),

    @Id
    val location: UserSmallholdingPoint = UserSmallholdingPoint(0, 0),

    @Column
    val smallholdingSize: Float? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val cashCrop: Crop? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val soilType: SoilType? = null,

    )


data class UserSmallholdingId(
    val user: User? = null,
    val location: UserSmallholdingPoint? = null
) : Serializable