package com.aes.common.logging

import com.aes.common.logging.Logging
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * Extension that returns a logger object that can be used by any class implementing
 * the [Logging] interface.
 */
fun <T : Logging> T.logger(): Logger = getLogger(javaClass)
