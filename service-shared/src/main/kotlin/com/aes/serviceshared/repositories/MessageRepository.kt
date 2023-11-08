package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: CrudRepository<Message, String>