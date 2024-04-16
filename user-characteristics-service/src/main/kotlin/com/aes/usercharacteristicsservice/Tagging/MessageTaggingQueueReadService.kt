package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Entities.Message
import com.aes.common.Queue.LocalQueueService
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MessageTaggingQueueReadService(
    val localQueueService: LocalQueueService,
    val messageTaggingService: MessageTaggingService
) : Logging {


    /**
     * Reads message-tag-queue every 1 second.
     * */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun readFromQueue() {
        logger().info("Reading messages from queue")
        var count = 0
        var hasMore = true
        val asyncOps = mutableListOf<Deferred<Unit>>()
        while (hasMore) {
            val result = localQueueService.withQueueItemAtPositionAsync<Message>("message_tag_queue", count) {
                messageTaggingService.tagMessage(this)
                true
            }
            if (result == null) {
                hasMore = false
            } else {
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