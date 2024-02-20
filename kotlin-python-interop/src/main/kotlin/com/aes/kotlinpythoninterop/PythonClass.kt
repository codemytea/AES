package com.aes.kotlinpythoninterop

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotations

abstract class PythonClass {

    inline fun <reified T> execute(function: KFunction<T>, vararg args: Any?): T{
        val annot = function.findAnnotations(PythonFunction::class).firstOrNull()
        val functionName = annot?.functionName ?: function.name
        val scriptName = annot?.scriptName ?: "main.py"

        return executeProgram(scriptName, functionName, args.toList())
    }

    fun getPythonProgram(filename: String): String{
        return this::class.java.classLoader.getResource(filename)?.path ?: filename
    }

    class ArgsWrapper(val args: List<Any?>)

    fun getPathForFile(filename: String): String{
        return this::class.java.classLoader.getResource("kotlinInterop.py")?.path?.replace("kotlinInterop.py", filename)
            ?: throw Exception("kotlinInterop.py does not exist (required for interoperability)")
    }

    fun writeArgumentsToFile(args: List<Any?>, filename: String){
        val wrapper = ArgsWrapper(args)
        val file = File(getPathForFile(filename))
        val mapper = jacksonObjectMapper().registerModules(JavaTimeModule())
        mapper.writeValue(file, wrapper)
    }

    inline fun <reified T> readResultFromFile(filename: String):T{
        val file = File(getPathForFile(filename))
        val mapper = jacksonObjectMapper().registerModules(JavaTimeModule())
        val (result, error) = file.readLines()
        if(error != "null") throw PythonException(error)
        val resultObj = mapper.readValue(result, T::class.java)
        return resultObj
    }
    inline fun <reified T> executeProgram(scriptName: String, functionName: String, arguments: List<Any?>): T{
        val programRunUID = UUID.randomUUID().toString()
        writeArgumentsToFile(arguments, "$programRunUID.args.json")
        val outputFile = File(getPathForFile("$programRunUID.out.txt"))
        outputFile.writeText("")
        val process = ProcessBuilder()
            .command(mutableListOf("python3", getPythonProgram(scriptName), programRunUID, functionName, getPathForFile("")))
            .redirectOutput(outputFile)
            .redirectError(outputFile)
            .start()
        val job = CoroutineScope(Dispatchers.IO).async {
            var currentLines = 0
            while(true) {
                delay(100)
                val lines = outputFile.readLines()
                for (i in currentLines until lines.size) {
                    println(lines[i])
                }
                currentLines = lines.size
            }
        }
        val result = process.onExit().join()
        runBlocking {
            job.cancelAndJoin()
        }
        return readResultFromFile<T>("$programRunUID.result.json")
    }
}