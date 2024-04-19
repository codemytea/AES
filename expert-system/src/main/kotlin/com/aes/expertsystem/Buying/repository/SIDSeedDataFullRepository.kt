package com.aes.expertsystem.Buying.repository

import com.aes.expertsystem.Buying.entities.SIDSeedDataFull
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Query to get information from the DB
 * */
@Repository
interface SIDSeedDataFullRepository : CrudRepository<SIDSeedDataFull, String> {
    fun findFirstByGenusAndEpithet(genus: String, epithet: String): SIDSeedDataFull?
}