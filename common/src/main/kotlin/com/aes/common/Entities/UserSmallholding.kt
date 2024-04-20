package com.aes.common.Entities

import com.aes.common.Enums.Crop
import com.aes.common.Enums.SoilType
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID


data class UserSmallholdingPoint(
    val lat: Int,
    var lng: Int
) : Serializable

@Entity
class UserSmallholding(

    @Id
    val id: UUID = UUID.randomUUID(),

    /**
     * The user
     * */
    @ManyToOne
    val user: User? = null,

    /**
     * The city the smallholding is in
     * */
    @Column
    var location_city: String? = null,

    /**
     * The country the smallholding is in
     * */
    @Column
    var location_country: String? = null,

    /**
     * The size of the smallholding
     * */
    @Column
    var smallholdingSize: Float? = null,

    /**
     * The main crop of the smallholding
     * */
    @Enumerated(value = EnumType.STRING)
    @Column
    var cashCrop: Crop? = null,

    /**
     * The soil type of the smallholding
     * */
    @Enumerated(value = EnumType.STRING)
    @Column
    val soilType: SoilType? = null,

    )


