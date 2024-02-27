package com.aes.common.Repositories

import com.aes.common.Entities.UserSmallholding
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSmallholdingRepository : CrudRepository<UserSmallholding, UUID> {
    fun findUserSmallholdingByUserId(id : UUID): UserSmallholding?
}