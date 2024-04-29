package com.aes.common.Repositories

import com.aes.common.Entities.User
import com.aes.common.Entities.UserSmallholding
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Query to get information from the DB
 * */
@Repository
interface UserSmallholdingRepository : CrudRepository<UserSmallholding, UUID> {
    fun findFirstByUser(user: User): UserSmallholding?
}
