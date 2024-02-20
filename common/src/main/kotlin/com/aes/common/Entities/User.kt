package com.aes.common.Entities

import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.common.Enums.LanguageCode
import jakarta.persistence.*
import java.util.*

@Entity
class User(
    /**
     * The unique id of the user
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    /**
     * The phone number(s) of the user. The user could have multiple phones/
     * change phone number. Don't want data to be lost.
     * */
    @ElementCollection
    val phoneNumber: List<Long> = mutableListOf(),

    /**
     * The language the user sends messages in and the language the system sends messages back in
     * */
    @Column
    @Enumerated(value = EnumType.STRING)
    var preferredLanguage: LanguageCode? = null,

    /**
     * The name of the user
     * */
    @Column
    val name: String? = null,

    /**
     * The age of the user
     * */
    @Column
    @Enumerated(value = EnumType.STRING)
    var age: Age? = null,

    /**
     * The gender of the user
     * */
    @Column
    @Enumerated(value = EnumType.STRING)
    var gender: Gender? = null,

    /**
     * The literacy level of the user
     * */
    @Column
    var literacy: Float? = null,

    @OneToMany
    @JoinColumn(name="userId", referencedColumnName = "id")
    val knowledgeAreas: MutableList<UserKnowledge> = mutableListOf()
)
