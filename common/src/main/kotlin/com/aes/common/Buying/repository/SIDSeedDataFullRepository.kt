package com.aes.common.Buying.repository

import com.aes.common.Buying.entities.SIDSeedDataFull
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SIDSeedDataFullRepository: CrudRepository<SIDSeedDataFull, String>{
    fun findFirstByGenusAndEpithet(genus: String, epithet: String): SIDSeedDataFull?
}