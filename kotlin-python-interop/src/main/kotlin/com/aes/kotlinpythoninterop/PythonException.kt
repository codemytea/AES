package com.aes.kotlinpythoninterop

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class PythonException(message: String): Exception(jacksonObjectMapper().readValue(message, String::class.java))