package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.APP)
class NotificationService {
    companion object {
        private const val NOTIFICATION_GROUP_ID = "Commit Message Generator"

        fun showInfo(project: Project?, content: String) {
            show(project, content, NotificationType.INFORMATION)
        }

        fun showWarning(project: Project?, content: String) {
            show(project, content, NotificationType.WARNING)
        }

        fun showError(project: Project?, content: String) {
            show(project, content, NotificationType.ERROR)
        }

        private fun show(project: Project?, content: String, type: NotificationType) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP_ID)
                .createNotification(content, type)
                .notify(project)
        }
    }
}
