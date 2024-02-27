package com.aes.common.Repositories

import com.aes.common.Entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<User, UUID>{

    fun findByPhoneNumberContaining(phoneNumber: Long): User?

    fun findUserById(id : UUID): User?
}