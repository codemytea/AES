package com.aes.usercharacteristicsservice.Evaluators.Knowledge

import com.aes.smsservices.Repositories.MessageRepository
import com.aes.usercharacteristicsservice.Enums.Crop
import com.aes.usercharacteristicsservice.Enums.Gender
import com.aes.usercharacteristicsservice.Enums.Topic
import com.aes.usercharacteristicsservice.Python.AttributeEstimator
import com.aes.usercharacteristicsservice.Python.KnowledgeClassifiers
import org.springframework.stereotype.Service
import java.util.*

@Service
class KnowledgeEvaluator(
    private val knowledgeClassifier: KnowledgeClassifiers
) {
    fun getCropCycleTopicsOfMessage(message : String): Topic {
        return knowledgeClassifier.getCropCycleTopicsOfMessage(listOf(message))
    }

    fun getCropTopicsOfMessage(message : String): Crop {
        return knowledgeClassifier.getCropTopicsOfMessage(listOf(message))
    }
}

/**
 * I want to:
 *
 * get a message in
 * tag it appropriately
 *
 * get a list of all tags into a map with tag: count
 * tag with highest count is least knowledgeable
 * tag with lowest is most
 * */