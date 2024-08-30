package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.diagnostic.thisLogger

object CustomLogger {
    private val logger = thisLogger()

    fun info(message: String, addCallerInfo: Boolean = false) {
        log(LogLevel.INFO, message, null, addCallerInfo)
    }

    fun warn(message: String, addCallerInfo: Boolean = false) {
        log(LogLevel.WARN, message, null, addCallerInfo)
    }

    fun error(message: String, throwable: Throwable? = null, addCallerInfo: Boolean = false) {
        log(LogLevel.ERROR, message, throwable, addCallerInfo)
    }

    fun debug(message: String, addCallerInfo: Boolean = false) {
        if (logger.isDebugEnabled) {
            log(LogLevel.DEBUG, message, null, addCallerInfo)
        }
    }

    private fun log(level: LogLevel, message: String, throwable: Throwable?, addCallerInfo: Boolean) {
        val finalMessage = if (addCallerInfo) addCallerInfo(message) else message
        when (level) {
            LogLevel.INFO -> logger.info(finalMessage)
            LogLevel.WARN -> logger.warn(finalMessage)
            LogLevel.ERROR -> logger.error(finalMessage, throwable)
            LogLevel.DEBUG -> logger.debug(finalMessage)
        }
    }

    private fun addCallerInfo(message: String): String {
        val caller = Thread.currentThread().stackTrace[4]
        return "[${caller.className.substringAfterLast('.')}.${caller.methodName}] $message"
    }

    private enum class LogLevel { INFO, WARN, ERROR, DEBUG }
}
