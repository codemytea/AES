package com.aes.usercharacteristicsservice.Python

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class PythonException(message: String): Exception(jacksonObjectMapper().readValue(message, String::class.java))