package com.aes.common.Entities

import jakarta.persistence.*
import java.util.UUID

@Entity
class UserFeedback(
    /**
     * The unique ID
     * */
    @Id
    var id: UUID = UUID.randomUUID(),
    /**
     * Who the message is actually associated with
     * */
    @ManyToOne
    var user: User = User(),
    /**
     * The feedback
     * */
    @Lob
    @Column(length = 5000)
    var feedback: String = "",
)
