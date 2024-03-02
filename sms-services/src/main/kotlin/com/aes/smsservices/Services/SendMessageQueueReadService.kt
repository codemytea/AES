package com.aes.usercharacteristicsservice.Tagging

import com.aes.common.Models.NewMessageDTO
import com.aes.common.Queue.LocalQueueService
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.smsservices.Services.SendSmsService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MessageTaggingQueueReadService(
    val localQueueService: LocalQueueService,
    val sendSmsService: SendSmsService
) : Logging {


    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun readFromQueue() {
        logger().info("Reading messages from queue")
        var count = 0
        var hasMore = true
        val asyncOps = mutableListOf<Deferred<Unit>>()
        while (hasMore) {
            val result = localQueueService.withQueueItemAtPositionAsync<NewMessageDTO>("send_message_queue", count) {
                sendSmsService.sendMessage(this)
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