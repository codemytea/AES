package com.aes.kotlinpythoninterop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinPythonInteropApplication

fun main(args: Array<String>) {
    runApplication<KotlinPythonInteropApplication>(*args)
}
