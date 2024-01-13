package com.aes.smsservices.Entities

import com.aes.smsservices.Enums.Gender
import com.aes.smsservices.Enums.LanguageCode
import jakarta.persistence.*
import java.io.Serializable
import java.util.*
import kotlin.collections.List

@Entity
class User(
    /**
     * The unique id of the user
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    /**
     * The phone number(s) of the user. The user could have multiple phones/
     * change phone number. Don't want data to be lost.
     * */
    @ElementCollection
    val phoneNumber: List<Long> = listOf(),

    /**
     * The language the user sends messages in and the language the system sends messages back in
     * */
    @Column
    val preferredLanguage: LanguageCode?,

    /**
     * The name of the user
     * */
    @Column
    val name: String? = null,

    /**
     * The age of the user
     * */
    @Column
    val age: Int? = null,

    /**
     * The gender of the user
     * */
    @Column
    val gender: Gender? = null,

    /**
     * The literacy level of the user
     * */
    @Column
    val literacy: Float? = null,
)
