package com.aes.common.Ecocrop.repository

import com.aes.common.Ecocrop.entities.EcocropData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EcocropDataRepository : CrudRepository<EcocropData, String> {
    fun findFirstByScientificName(scientificName: String): EcocropData?
}