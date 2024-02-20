package com.aes.common.Queue

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import java.io.File

@Service
class LocalQueueService {

    val MAX_QUEUE_SIZE = 999
    val queueDir = {queueName: String-> "queues/$queueName"}

    val mapper = jacksonObjectMapper().registerModules(JavaTimeModule())
    private fun getQueueItemName(position: Int): String{
        return "$position.item"
    }

    private fun createQueueIfNeeded(queueName: String){
        val fileTop = File("queues")
        if(!fileTop.exists()) fileTop.mkdir()
        val file = File(queueDir(queueName))
        if(file.exists()) return
        else file.mkdir()
    }

    fun getQueuePosition(itemName : String): Int{
        return itemName.removeSuffix(".item").toInt()
    }

    fun getQueueDirectory(queueName: String): File{
        createQueueIfNeeded(queueName)
        return File(queueDir(queueName))
    }

    fun getAllQueueItems(queueName: String): List<File>{
        val queueDir = getQueueDirectory(queueName)
        return queueDir.walk().maxDepth(1).map { it.absoluteFile }.toList().filter { it.name != queueDir.name }
    }

    fun getMinQueueItem(queueName: String): File?{
        return getAllQueueItems(queueName).minByOrNull {
            getQueuePosition(it.name)
        }
    }

    fun getQueueItemAtPosition(queueName: String, position: Int): File?{
        return getAllQueueItems(queueName).sortedBy {
            getQueuePosition(it.name)
        }.getOrNull(position)
    }

    fun getMaxQueueItem(queueName: String): File?{
        return getAllQueueItems(queueName).maxByOrNull {
            getQueuePosition(it.name)
        }
    }


    fun popTopQueueItem(queueName: String){
        getMinQueueItem(queueName)?.delete()
    }

    fun getNextQueuePosition(queueName: String): Int{
        val currentMaxQueueItem = getMaxQueueItem(queueName) ?: return 0
        val currentPos = getQueuePosition(currentMaxQueueItem.name)
        return if(currentPos == MAX_QUEUE_SIZE) 0
        else currentPos + 1
    }

    fun getNextQueueItemFile(queueName: String): File{
        while(true) {
            val position = getNextQueuePosition(queueName)
            val itemName = getQueueItemName(position)
            createQueueIfNeeded(queueName)
            val file = File("${queueDir(queueName)}/$itemName")
            if(file.createNewFile()){
                return file
            }
        }
    }


    inline fun <reified T> readTopQueueItem(queueName: String): T?{
        val topQueueItemFile = getMinQueueItem(queueName) ?: return null
        return mapper.readValue(topQueueItemFile, T::class.java)
    }

    inline fun <reified T> readQueueItemAtPosition(queueName: String, position: Int): T?{
        val itemFile = getQueueItemAtPosition(queueName, position) ?: return null
        return mapper.readValue(itemFile, T::class.java)
    }

    inline fun <reified T> withLatestQueueItem(queueName: String, block: T.()->Boolean): Boolean{
        val obj = readTopQueueItem<T>(queueName) ?: return false
        if(block(obj)){
            popTopQueueItem(queueName)
        }
        return true
    }

    inline fun <reified T> withQueueItemAtPosition(queueName: String, position: Int, block: T.()->Boolean): Boolean{
        val obj = readQueueItemAtPosition<T>(queueName, position) ?: return false
        if(block(obj)){
            popTopQueueItem(queueName)
        }
        return true
    }

    inline fun <reified T> withQueueItemAtPositionAsync(queueName: String, position: Int, crossinline block: T.()->Boolean): Deferred<Unit>?{
        val obj = readQueueItemAtPosition<T>(queueName, position) ?: return null
        return CoroutineScope(Dispatchers.IO).async {
            if(block(obj)){
                popTopQueueItem(queueName)
            }
        }
    }

    fun <T> writeItemToQueue(queueName: String, item: T){
        val nextPos = getNextQueuePosition(queueName)
        val queueItemFile = getNextQueueItemFile(queueName)
        mapper.writeValue(queueItemFile, item)
    }

}