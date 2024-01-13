package com.aes.smsservices.Repositories

import com.aes.smsservices.Entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<User, UUID>{

    fun findByPhoneNumberContaining(phoneNumber: Long): User?
}