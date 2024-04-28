package com.aes.smsservices.Configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("com.aes.smsservice")
class SmsServiceConfiguration(
    val gatewayApiKey: String
)