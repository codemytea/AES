package com.aes.usercharacteristicsservice.Python

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
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
        writeArgumentsToFile(arguments, "args.json")
        val process = Runtime.getRuntime().exec(arrayOf("python3", getPythonProgram(scriptName), functionName, getPathForFile("")))
        process.onExit().join()
        return readResultFromFile<T>("result.json")
    }
}