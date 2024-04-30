package com.aes.common.Queue

import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

/**
 * Own Queue structure used throughout the project. Cloud queues such as AWS SQS could be used by they incur a significant cost.
 * Some functions are unused in this project but have been added to create a holistic queue structure ready for publishing
 * */
@Service
class LocalQueueService : Logging {
    fun String.pathParent(): String? {
        val lastSlash = lastIndexOf('/')
        return substring(0, lastSlash).ifBlank { null }
    }

    val topLevelDir by lazy {
        var currentPath = Paths.get("").absolutePathString()
        while (true) {
            if (File("$currentPath/gradlew.bat").exists()) {
                return@lazy currentPath
            } else {
                currentPath.pathParent()?.let {
                    currentPath = it
                } ?: throw Exception("Invalid folder structure, make sure you have the gradlew script")
            }
        }
    }

    val MAX_QUEUE_SIZE = 999 // If it's less, risk losing items in queue, if more, means possible error in system
    val queueRoot = File("$topLevelDir/queues")
    val queueDir = { queueName: String -> "${queueRoot.absolutePath}/$queueName" } // the directory the queue is stored in
    val mapper = jacksonObjectMapper().registerModules(JavaTimeModule()) // JSON converter

    /**
     * Gets the name of an item at a specific position of the queue
     *
     * @param position The position in the queue
     * @return the name
     * */
    private fun getQueueItemName(position: Int): String {
        return "$position.item"
    }

    /**
     * Creates the queue if it doesn't already exist by finding the queue storage directory (or creating it if it doesn't exist)
     * Then creates the subdirectory that this specific queue will be in. If it already exists, returns
     *
     * @param queueName The name of the queue to be created
     * */
    private fun createQueueIfNeeded(queueName: String) {
        val fileTop = queueRoot
        if (!fileTop.exists()) fileTop.mkdir()
        val file = File(queueDir(queueName))
        if (file.exists()) {
            return
        } else {
            file.mkdir()
        }
    }

    /**
     * Gets the position of an item with a specific name in the queue
     *
     * @param itemName the name of the item
     * @return the position of that item in the queue
     * */
    fun getQueuePosition(itemName: String): Int {
        return itemName.removeSuffix(".item").toInt()
    }

    /**
     * Returns the root directory of the queue
     *
     * @param queueName the name of the queue you are trying to get the root directory of
     * @return the directory
     * */
    fun getQueueDirectory(queueName: String): File {
        createQueueIfNeeded(queueName)
        return File(queueDir(queueName))
    }

    /**
     * Gets all items in a queue given the queues name
     *
     * @param queueName the name of the queue
     * @return A list of queue items (each item is a separate txt file)
     * */
    fun getAllQueueItems(queueName: String): List<File> {
        val queueDir = getQueueDirectory(queueName)
        return queueDir.walk().maxDepth(1).map { it.absoluteFile }.toList().filter { it.name != queueDir.name }
    }

    /**
     * Gets the queue item that should be processed first (FIFO) given the queues name
     *
     * @param queueName the name of the queue
     * @return the item to be processed (a txt file)
     * */
    fun getMinQueueItem(queueName: String): File? {
        return getAllQueueItems(queueName).minByOrNull {
            getQueuePosition(it.name)
        }
    }

    /**
     * Gets a queue item given a specific position in a qiven queue
     *
     * @param queueName the name of the queue
     * @param position the position the item is in
     * @return the queue item (a txt file)
     * */
    fun getQueueItemAtPosition(
        queueName: String,
        position: Int,
    ): File? {
        return getAllQueueItems(queueName).sortedBy {
            getQueuePosition(it.name)
        }.getOrNull(position)
    }

    /**
     * Gets the current top items of the queue (the last item to be processed). Used to determine where additional
     * incoming items should be positioned
     *
     * @param queueName the name of the queue
     * @return the top item (a txt file)
     * */
    fun getMaxQueueItem(queueName: String): File? {
        return getAllQueueItems(queueName).maxByOrNull {
            getQueuePosition(it.name)
        }
    }

    /**
     * Deletes the item in the queue that has just been processed (the first one as FIFO structure)
     *
     * @param queueName the name of the queue
     * */
    fun popTopQueueItem(queueName: String) {
        logger().info("Popping queue item from $queueName")
        getMinQueueItem(queueName)?.delete()
    }

    /**
     * Gets the next available queue position. If no items in the queue, the next available position is 0. If
     * there are items in the queue, and they are below the MAX_QUEUE_SIZE, return one above that. Else, wrap round
     * and start at 0 again.
     *
     * @param queueName the name of they queue you are getting the next available position for
     * @return the next available position (int)
     * */
    fun getNextQueuePosition(queueName: String): Int {
        val currentMaxQueueItem = getMaxQueueItem(queueName) ?: return 0
        val currentPos = getQueuePosition(currentMaxQueueItem.name)
        return if (currentPos == MAX_QUEUE_SIZE) {
            0
        } else {
            currentPos + 1
        }
    }

    /**
     * Creates a new file for the next item to be put on the queue.
     *
     * @param queueName the name of the queue you want to make a new item on
     * @return the new file
     * */
    fun getNextQueueItemFile(queueName: String): File {
        while (true) {
            val position = getNextQueuePosition(queueName)
            val itemName = getQueueItemName(position)
            createQueueIfNeeded(queueName)
            val file = File("${queueDir(queueName)}/$itemName")
            if (file.createNewFile()) {
                return file
            }
        }
    }

    /**
     * Reads the top item of a queue using it's given type
     *
     * @param queueName the name of the queue you are getting the top item of
     * @return the item
     * */
    inline fun <reified T> readTopQueueItem(queueName: String): T? {
        val topQueueItemFile = getMinQueueItem(queueName) ?: return null
        return mapper.readValue(topQueueItemFile, T::class.java)
    }

    /**
     * Reads the item of a queue in a given position
     *
     * @param queueName the name of the queue
     * @param position the position in the queue
     * @return the item
     * */
    inline fun <reified T> readQueueItemAtPosition(
        queueName: String,
        position: Int,
    ): T? {
        val itemFile = getQueueItemAtPosition(queueName, position) ?: return null
        return mapper.readValue(itemFile, T::class.java)
    }

    /**
     * Performs function 'T.()' on the next item in the queue to be processed. If this function returns true, pop this
     * item after the function has been executed. If the function returns False, don't pop the item.
     *
     * @param queueName the name of the queue
     * @param block the function to be executed
     * @return if the operation has successfully been performed
     * */
    inline fun <reified T> withLatestQueueItem(
        queueName: String,
        block: T.() -> Boolean,
    ): Boolean {
        val obj = readTopQueueItem<T>(queueName) ?: return false
        if (block(obj)) {
            popTopQueueItem(queueName)
        }
        return true
    }

    /**
     * Performs function 'T.()' on an item at a given position in the queue to be processed. If this function returns true, pop this
     * item after the function has been executed. If the function returns False, don't pop the item.
     *
     * @param queueName the name of the queue
     * @param position the position of the item to perform the function on
     * @param block the function to be executed
     * @return if the operation has successfully been performed
     * */
    inline fun <reified T> withQueueItemAtPosition(
        queueName: String,
        position: Int,
        block: T.() -> Boolean,
    ): Boolean {
        val obj = readQueueItemAtPosition<T>(queueName, position) ?: return false
        if (block(obj)) {
            popTopQueueItem(queueName)
        }
        return true
    }

    /**
     * Performs function 'T.()' on an item at a given position in the queue to be processed asynchronously.
     * This is done if items don't depend on each other as parallelisation speeds processing up.
     * If this function returns true, pop this item after the function has been executed. If the function
     * returns False, don't pop the item.
     *
     * @param queueName the name of the queue
     * @param position the position of the item to perform the function on
     * @param block the function to be executed async
     * @return if the operation has successfully been performed
     * */
    inline fun <reified T> withQueueItemAtPositionAsync(
        queueName: String,
        position: Int,
        crossinline block: T.() -> Boolean,
    ): Deferred<Unit>? {
        val obj = readQueueItemAtPosition<T>(queueName, position) ?: return null
        return CoroutineScope(Dispatchers.IO).async {
            if (block(obj)) {
                popTopQueueItem(queueName)
            }
        }
    }

    /**
     * Given a specific queue, write a new item to it in the next available position.
     *
     * @param queueName the name of the queue to add the new item to
     * @param item the item to add to the queue
     * */
    fun <T> writeItemToQueue(
        queueName: String,
        item: T,
    ) {
        val nextPos = getNextQueuePosition(queueName)
        val queueItemFile = getNextQueueItemFile(queueName)
        mapper.writeValue(queueItemFile, item)
    }
}
