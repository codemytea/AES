package com.aes.kotlinpythoninterop

/**
 * Custom annotation to apply to Kotlin functions to link it to a Python function
 * */
@Target(AnnotationTarget.FUNCTION)
annotation class PythonFunction(val functionName: String, val scriptName: String)
