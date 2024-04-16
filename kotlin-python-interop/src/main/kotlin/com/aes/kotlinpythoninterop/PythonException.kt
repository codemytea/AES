package com.aes.kotlinpythoninterop

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Python Exception thrown if Python Execution fails
 * */
class PythonException(message: String) : Exception(jacksonObjectMapper().readValue(message, String::class.java))