package com.aes.expertsystem.Data.Trefle

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("com.aes.expertsystem.trefle")
class TrefleConfiguration (
    val apiKey: String
)