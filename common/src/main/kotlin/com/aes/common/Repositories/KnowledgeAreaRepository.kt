package com.aes.common.Repositories

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.KnowledgeAreaId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KnowledgeAreaRepository : CrudRepository<KnowledgeArea, KnowledgeAreaId>
