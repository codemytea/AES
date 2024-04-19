package com.aes.expertsystem.Ecocrop.repository

import com.aes.expertsystem.Ecocrop.entities.EcocropData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EcocropDataRepository : CrudRepository<EcocropData, String> {
    fun findFirstByScientificName(scientificName: String): EcocropData?
}