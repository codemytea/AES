package com.aes.common.Enums

/**
 * Status of messages
 * */
enum class MessageStatus {
    PENDING,
    DELIVERED,
    FAILED,
    ENROUTE,
    REJECTED,
    UNDELIVERABLE
}