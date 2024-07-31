package com.github.guswlsdl0121.messagemaker.utils

import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

object ActionUtils {
    fun AnActionEvent.executeWithErrorHandling(action: (Project) -> Unit) {
        try {
            val project = this.project ?: throw IllegalStateException("프로젝트가 null입니다.")
            action(project)
        } catch (ex: Exception) {
            LogWrapper.info("프로젝트가 지정되지 않았습니다.")
            project?.let { NotificationService.showError(it, "액션 실행 중 오류가 발생했습니다: ${ex.message}") }
        }
    }
}
