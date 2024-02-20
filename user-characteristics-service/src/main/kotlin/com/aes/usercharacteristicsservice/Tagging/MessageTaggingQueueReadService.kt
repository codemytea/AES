package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Entities.KnowledgeArea
import com.aes.common.Entities.Message
import com.aes.common.Entities.MessageTopics
import com.aes.common.Queue.LocalQueueService
import com.aes.common.Repositories.KnowledgeAreaRepository
import com.aes.common.Repositories.MessageTopicsRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Evaluators.Knowledge.KnowledgeEvaluator
import jakarta.transaction.Transactional
import kotlinx.coroutines.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MessageTaggingQueueReadService(
    val localQueueService: LocalQueueService,
    val messageTaggingService: MessageTaggingService
): Logging {




    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun readFromQueue(){
        logger().info("Reading messages from queue")
        var count = 0
        var hasMore = true
        val asyncOps = mutableListOf<Deferred<Unit>>()
        while(hasMore){
            val result = localQueueService.withQueueItemAtPositionAsync<Message>("message_tag_queue", count) {
                messageTaggingService.tagMessage(this)
                true
            }
            if(result == null){
                hasMore = false
            } else{
                asyncOps.add(result)
            }
            count++
        }
        runBlocking {
            asyncOps.awaitAll()
        }
        logger().info("${asyncOps.size} messages read")
    }

}