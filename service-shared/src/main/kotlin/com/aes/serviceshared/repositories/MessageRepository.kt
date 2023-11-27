package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository: CrudRepository<Message, UUID>





