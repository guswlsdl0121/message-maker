package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.Notification
import com.github.guswlsdl0121.messagemaker.utils.LogFactory
import com.intellij.openapi.project.Project

object ActionExceptionHandler {
    fun handle(project: Project?, ex: Exception) {
        when (ex) {
            is MessageMakerException -> handleMessageMakerException(project, ex)
            else -> handleUnexpectedException(project, ex)
        }
    }

    private fun handleMessageMakerException(project: Project?, ex: MessageMakerException) {
        when (ex) {
            is NoChangesException -> {
                LogFactory.warn(ex.message ?: "변경사항이 없습니다.")
                project?.let { Notification.NO_CHANGES_DETECTED.show(project) }
            }

            is ProjectNullException -> {
                LogFactory.warn(ex.message ?: "프로젝트가 null입니다.")
                project?.let { Notification.GENERATION_FAILED.show(project) }
            }
        }
    }

    private fun handleUnexpectedException(project: Project?, ex: Exception) {
        LogFactory.error("예상치 못한 오류 발생", ex)
        project?.let { Notification.GENERATION_FAILED.show(project, ex.message) }
    }
}
