package com.aes.common.Entities

import com.aes.common.Enums.Age
import com.aes.common.Enums.Crop
import com.aes.common.Enums.Gender
import com.aes.common.Enums.LanguageCode
import jakarta.persistence.*
import java.time.LocalDateTime
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
     * change phone number. Don't want Data to be lost.
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
    var name: String? = null,
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
    /**
     * Whether or not the user has asked to stop having information collected about them
     * */
    @Column
    var stopCollectingInformation: Boolean = false,
    /**
     * Whether or not the user has asked to stop sending them notifications
     * */
    @Column
    var stopSendingNotifications: Boolean = false,
    /**
     * A users knowledge areas
     * */
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val knowledgeAreas: MutableList<UserKnowledge> = mutableListOf(),
    /**
     * the last time a user has interacted with a certain knowledge area
     * */
    @ElementCollection
    @CollectionTable(
        name = "user_knowledge_last_interaction",
        joinColumns = [JoinColumn(name = "user_id")],
    )
    @MapKeyJoinColumns(
        MapKeyJoinColumn(name = "knowledge_area_crop_name"),
        MapKeyJoinColumn(name = "knowledge_area_topic"),
    )
    @Column(name = "last_interaction_time")
    val lastInteractionTime: MutableMap<KnowledgeArea, LocalDateTime> = mutableMapOf(),
    /**
     * The user may have multiple smallholdings they want to ask questions about
     * */
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val userSmallholdingInfo: MutableList<UserSmallholding> = mutableListOf(),
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val messages: MutableList<Message> = mutableListOf(),
) {
    fun crops(): List<Crop> {
        return userSmallholdingInfo.mapNotNull { it.cashCrop }
    }

    fun updateLastInteractionTime(
        knowledgeArea: KnowledgeArea,
        time: LocalDateTime,
    ) {
        lastInteractionTime[knowledgeArea] = time
    }
}
