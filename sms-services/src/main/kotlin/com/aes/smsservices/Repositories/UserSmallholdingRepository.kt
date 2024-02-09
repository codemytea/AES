package com.aes.smsservices.Repositories

import com.aes.common.Entities.User
import com.aes.smsservices.Entities.UserSmallholding
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSmallholdingRepository: CrudRepository<UserSmallholding, User> {
}