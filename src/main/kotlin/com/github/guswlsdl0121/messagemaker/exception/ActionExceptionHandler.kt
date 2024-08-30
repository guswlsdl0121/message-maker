package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.CommitMessageNotification
import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.github.guswlsdl0121.messagemaker.utils.CustomLogger
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class ActionExceptionHandler(private val project: Project) {
    fun handle(ex: Exception) {
        when (ex) {
            is CustomException -> handlerCustomException(ex)
            else -> handleUnexpectedException(ex)
        }
    }

    private fun handlerCustomException(ex: CustomException) {
        val notificationService = project.service<NotificationService>()
        when (ex) {
            is NoChangesException -> {
                CustomLogger.warn(ex.message ?: "변경사항이 없습니다.")
                notificationService.show(CommitMessageNotification.NO_CHANGES_DETECTED)
            }

            is ProjectNullException -> {
                CustomLogger.warn(ex.message ?: "프로젝트가 null입니다.")
                notificationService.show(CommitMessageNotification.PROJECT_NOT_FOUND)
            }
        }
    }

    private fun handleUnexpectedException(ex: Exception) {
        CustomLogger.error("예상치 못한 오류 발생", ex, true)
        val notificationService = project.service<NotificationService>()
        notificationService.show(CommitMessageNotification.UNEXPECTED_ERROR, ex.message)
    }
}
