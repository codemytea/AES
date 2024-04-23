package com.aes.common.Repositories

import com.aes.common.Entities.User
import com.aes.common.Models.RecipientDTO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Queries to get information from the DB
 * */
@Repository
interface UserRepository: CrudRepository<User, UUID>{

    fun findByPhoneNumberContaining(phoneNumber: Long): User?

    fun findUserById(id: UUID): User?

    fun findAllByMessagesIsEmpty(): List<User>?
}

fun UserRepository.findAllPhoneNumbersByMessagesIsEmpty(): List<RecipientDTO>? {
    return findAllByMessagesIsEmpty()?.map { RecipientDTO(it.phoneNumber[0]) }
}