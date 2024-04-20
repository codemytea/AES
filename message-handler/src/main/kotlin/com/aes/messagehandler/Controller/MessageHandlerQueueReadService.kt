package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Entities.Message
import com.aes.common.Models.MessageQueueItem
import com.aes.common.Queue.LocalQueueService
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.messagehandler.Services.MessageHandlerService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MessageTaggingQueueReadService(
    val localQueueService: LocalQueueService,
    val messageHandlerService: MessageHandlerService
) : Logging {


    /**
     * Reads message-handler-queue every 1 second. May take longer to execute entire message handling pipeline for all
     * messages currently in the queue, in which case, it only reads the queue again after the current messages on the
     * queue have finished processing.
     * */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun readFromQueue() {
        logger().info("Reading messages from message-handler-queue")
        var count = 0
        var hasMore = true
        val asyncOps = mutableListOf<Deferred<Unit>>()
        while (hasMore) {
            val result = localQueueService.withQueueItemAtPositionAsync<MessageQueueItem>("message_handler_queue", count) {
                messageHandlerService.handleRequest(this)
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