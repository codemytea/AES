package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository: CrudRepository<Message, Long>





