package com.aes.smsservices.Services

import com.aes.common.Models.NewMessageDTO
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
class SendMessageQueueReadService(
    val localQueueService: LocalQueueService,
    val sendSmsService: SendSmsService
) : Logging {



    /**
     * Checks the send-message-queue every second for messages to send. For each message received, sendSmsService is
     * called which send the message to the appropriate user.
     * */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun readFromQueue() {
        logger().info("Reading messages from queue")
        var count = 0
        var hasMore = true
        val asyncOps = mutableListOf<Deferred<Unit>>()
        while (hasMore) {
            val result = localQueueService.withQueueItemAtPositionAsync<NewMessageDTO>("send_message_queue", count) {
                sendSmsService.sendSMS(this)
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