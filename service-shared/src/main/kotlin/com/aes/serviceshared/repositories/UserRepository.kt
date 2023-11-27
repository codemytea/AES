package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<User, UUID>{}