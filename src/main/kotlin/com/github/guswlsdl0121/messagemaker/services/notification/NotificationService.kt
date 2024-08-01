package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.APP)
class NotificationService {
    companion object {
        private const val NOTIFICATION_GROUP_ID = "Commit Message Generator"
        private const val PLUGIN_NAME = "MessageMaker"

        fun show(project: Project?, title: String, content: String, type: NotificationType) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP_ID)
                .createNotification(createTitle(title), content, type)
                .notify(project)
        }

        private fun createTitle(title: String): String {
            return "$PLUGIN_NAME: $title"
        }

        fun getPluginName(): String = PLUGIN_NAME
    }
}
