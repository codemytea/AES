package com.aes.common.Repositories

import com.aes.common.Entities.UserFeedback
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserFeedbackRepository : CrudRepository<UserFeedback, UUID>