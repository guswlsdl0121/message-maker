package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class NotificationService(private val project: Project) {
    fun show(commitMessageNotification: CommitMessageNotification, vararg args: Any?) {
        val formattedContent = commitMessageNotification.content.format(*args)
        show(commitMessageNotification.title, formattedContent, commitMessageNotification.type)
    }

    private fun show(title: String, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(NotificationConstants.NOTIFICATION_GROUP_ID)
            .createNotification(createTitle(title), content, type)
            .notify(project)
    }

    private fun createTitle(title: String): String {
        return "${NotificationConstants.PLUGIN_NAME}: $title"
    }
}
