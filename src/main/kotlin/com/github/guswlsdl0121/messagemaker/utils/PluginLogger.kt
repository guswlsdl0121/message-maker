package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.diagnostic.thisLogger

object PluginLogger {
    private val logger = thisLogger()

    fun info(message: String, addCallerInfo: Boolean = false) {
        logger.info(if (addCallerInfo) addCallerInfo(message) else message)
    }

    fun warn(message: String, addCallerInfo: Boolean = false) {
        logger.warn(if (addCallerInfo) addCallerInfo(message) else message)
    }

    fun error(message: String, throwable: Throwable? = null, addCallerInfo: Boolean = false) {
        logger.error(if (addCallerInfo) addCallerInfo(message) else message, throwable)
    }

    fun debug(message: String, addCallerInfo: Boolean = false) {
        if (logger.isDebugEnabled) {
            logger.debug(if (addCallerInfo) addCallerInfo(message) else message)
        }
    }

    private fun addCallerInfo(message: String): String {
        val stackTrace = Thread.currentThread().stackTrace
        // 0: getStackTrace, 1: addCallerInfo, 2: log function, 3: actual caller
        val caller = stackTrace[3]
        return "[${caller.className.substringAfterLast('.')}.${caller.methodName}] $message"
    }
}
