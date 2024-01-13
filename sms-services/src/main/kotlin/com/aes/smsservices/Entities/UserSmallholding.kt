package com.aes.smsservices.Entities

import com.aes.smsservices.Enums.Crop
import com.aes.smsservices.Enums.SoilType
import jakarta.persistence.*
import java.io.Serializable


data class UserSmallholdingPoint(
    val lat:Int,
    var lng: Int
): Serializable

@Entity
@IdClass(UserSmallholdingId::class)
class UserSmallholding(

    @Id
    @ManyToOne
    val user: User,

    @Id
    val location: UserSmallholdingPoint? = null,

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
): Serializable