package com.aes.common.Repositories

import com.aes.common.Entities.User
import com.aes.common.Entities.UserKnowledge
import com.aes.common.Entities.UserKnowledgeId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserKnowledgeRepository : CrudRepository<UserKnowledge, UserKnowledgeId>