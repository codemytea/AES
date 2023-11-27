package com.aes.serviceshared.entities

import com.aes.serviceshared.Models.Gender
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable
import java.time.LocalDate
import java.util.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column
    val name: String? = null,

    @Column
    val phoneNumber: String,

    @Column
    val age: Int? = null,

    @Enumerated(value = EnumType.STRING)
    @Column
    val gender: Gender? = null,

    @Column
    val literacy: Float? = null,
): Serializable
