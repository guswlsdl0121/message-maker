package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.Notification
import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.github.guswlsdl0121.messagemaker.utils.PluginLogger
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class ActionExceptionHandler(private val project: Project) {
    fun handle(ex: Exception) {
        when (ex) {
            is MessageMakerException -> handleMessageMakerException(ex)
            else -> handleUnexpectedException(ex)
        }
    }

    private fun handleMessageMakerException(ex: MessageMakerException) {
        val notificationService = project.service<NotificationService>()
        when (ex) {
            is NoChangesException -> {
                PluginLogger.warn(ex.message ?: "변경사항이 없습니다.")
                notificationService.show(Notification.NO_CHANGES_DETECTED)
            }
            is ProjectNullException -> {
                PluginLogger.warn(ex.message ?: "프로젝트가 null입니다.")
                notificationService.show(Notification.GENERATION_FAILED)
            }
        }
    }

    private fun handleUnexpectedException(ex: Exception) {
        PluginLogger.error("예상치 못한 오류 발생", ex, true)
        val notificationService = project.service<NotificationService>()
        notificationService.show(Notification.GENERATION_FAILED, ex.message)
    }
}
