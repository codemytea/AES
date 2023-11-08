package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.MessageLocalisation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageLocalisationRepository: CrudRepository<MessageLocalisation, String>