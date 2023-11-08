package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: CrudRepository<User, String>