package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.logger

object LogFactory {
    private inline fun <reified T : Any> getLogger(): Logger = logger<T>()

    fun info(message: String) {
        getLogger<Any>().info(addCallerInfo(message))
    }

    fun warn(message: String) {
        getLogger<Any>().warn(addCallerInfo(message))
    }

    fun error(message: String, throwable: Throwable? = null) {
        getLogger<Any>().error(addCallerInfo(message), throwable)
    }

    fun debug(message: String) {
        getLogger<Any>().debug(addCallerInfo(message))
    }

    private fun addCallerInfo(message: String): String {
        val stackTrace = Thread.currentThread().stackTrace
        // 0: getStackTrace, 1: addCallerInfo, 2: log function, 3: actual caller
        val caller = stackTrace[3]
        return "[${caller.className.substringAfterLast('.')}.${caller.methodName}] $message"
    }
}
