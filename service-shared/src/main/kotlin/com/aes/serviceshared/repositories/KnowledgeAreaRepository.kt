package com.aes.serviceshared.repositories

import com.aes.serviceshared.entities.KnowledgeAreaId
import com.aes.serviceshared.entities.KnowledgeArea
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KnowledgeAreaRepository: CrudRepository<KnowledgeArea, KnowledgeAreaId> {
}