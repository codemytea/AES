package com.aes.smsservices.Exceptions

import org.springframework.http.HttpStatusCode

class MessageRequestException(
    statusCode: HttpStatusCode
) : Exception("Request failed with status code ${statusCode.value()}")