package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.Gender
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate

@Entity
class User(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column
    val user_id: Long,

    @Column
    val name: String? = null,

    @Column
    val phone_number: String,

    @Column
    val location: String? = null,

    @Column
    val farm_size_acres: String? = null,

    @Column
    val soil_type: String? = null,

    @Column
    val main_crop_type: String? = null,

    @Column
    val age: Int? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val gender: Gender? = null,

    @OneToMany(fetch = FetchType.LAZY)
    @Column
    val message_history: List<Message>? = emptyList(),

    @Column
    val created_at: LocalDate? = null,

    @Column
    val updated_at: LocalDate? = null,
)