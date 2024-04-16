package com.aes.kotlinpythoninterop

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotations

abstract class PythonClass {

    /**
     * Executes a given Python script
     *
     * @param function - the corresponding kotlin function called to execute python function
     * @param args - the arguments to the python function
     * @return the result of the python function
     * */
    protected inline fun <reified T> execute(function: KFunction<T>, vararg args: Any?): T {
        val annot = function.findAnnotations(PythonFunction::class).firstOrNull()
        val functionName = annot?.functionName ?: function.name
        val scriptName = annot?.scriptName ?: "main.py"

        return executeProgram(scriptName, functionName, args.toList())
    }

    /**
     * Gets a reference to the Python program from the resources directory
     * @param filename - the name of the python file
     * @return the full path to the file
     * */
    fun getPythonProgram(filename: String): String {
        return this::class.java.classLoader.getResource(filename)?.path ?: filename
    }

    /**
     * Wraps args for JSON serialisation
     * */
    class ArgsWrapper(val args: List<Any?>)


    /**
     * Gets the location to store transient files during teh execution process
     *
     * @param filename - the name of the transient file
     * @return full path to the file which should be used for transient operations
     * */
    fun getPathForFile(filename: String): String {
        return this::class.java.classLoader.getResource("kotlinInterop.py")?.path?.replace("kotlinInterop.py", filename)
            ?: throw Exception("kotlinInterop.py does not exist (required for interoperability)")
    }


    /**
     * Writes the arguments to the transient file
     *
     * @param args - the args to write
     * @param filename - the file to write the args to
     * */
    fun writeArgumentsToFile(args: List<Any?>, filename: String) {
        val wrapper = ArgsWrapper(args)
        val file = File(getPathForFile(filename))
        val mapper = jacksonObjectMapper().registerModules(JavaTimeModule())
        mapper.writeValue(file, wrapper)
    }


    /**
     * Reads the result of the Python execution
     *
     * @param filename - the filename that the executor result is stored in
     * @return the result of the program
     * @throws PythonException if the python execution fails
     * */
    inline fun <reified T> readResultFromFile(filename: String): T {
        val file = File(getPathForFile(filename))
        val mapper = jacksonObjectMapper().registerModules(JavaTimeModule())
        val (result, error) = file.readLines()
        if (error != "null") throw PythonException(error)
        val resultObj = mapper.readValue(result, T::class.java)
        return resultObj
    }

    /**
     * Executes a certain function in a Python script
     *
     * @param scriptName the script in which the function is located
     * @param functionName the name of the function
     * @param arguments the arguments to the function
     * @return the result of the function
     * @throws PythonException if Python execution fails
     * */
    inline fun <reified T> executeProgram(scriptName: String, functionName: String, arguments: List<Any?>): T {
        val programRunUID = UUID.randomUUID().toString()
        writeArgumentsToFile(arguments, "$programRunUID.args.json")
        val outputFile = File(getPathForFile("$programRunUID.out.txt"))
        outputFile.writeText("")
        val process = ProcessBuilder()
            .command(
                mutableListOf(
                    "python3",
                    getPythonProgram(scriptName),
                    programRunUID,
                    functionName,
                    getPathForFile("")
                )
            )
            .redirectOutput(outputFile)
            .redirectError(outputFile)
            .start()
        val job = CoroutineScope(Dispatchers.IO).async {
            var currentLines = 0
            while (true) {
                delay(100)
                val lines = outputFile.readLines()
                for (i in currentLines until lines.size) {
                    println(lines[i])
                }
                currentLines = lines.size
            }
        }
        val result = process.onExit().join() // unused variable prevents warning being thrown in certain IDEs due to async process
        runBlocking {
            job.cancelAndJoin()
        }
        return readResultFromFile<T>("$programRunUID.result.json")
    }
}