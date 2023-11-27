package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.User
import com.aes.serviceshared.entities.UserSmallholding
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSmallholdingRepository: CrudRepository<UserSmallholding, User> {
}