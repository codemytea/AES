package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.User
import com.aes.serviceshared.entities.UserKnowledge
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserKnowledgeRepository : CrudRepository<UserKnowledge, User> {
}