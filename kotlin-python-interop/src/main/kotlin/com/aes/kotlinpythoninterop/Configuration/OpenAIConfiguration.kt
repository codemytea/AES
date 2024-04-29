package com.aes.kotlinpythoninterop.Configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("com.aes.kotlinpythoninterop")
class OpenAIConfiguration(
    val openAIApiKey: String = System.getenv("OPENAI_API_KEY"),
)
