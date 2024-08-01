package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

enum class Notification(
    private val title: String,
    private val content: String,
    private val type: NotificationType
) {
    NO_CHANGES_DETECTED(
        "No Changes Detected",
        "There are no changes to generate a commit message for.",
        NotificationType.WARNING
    ),
    GENERATION_FAILED(
        "Generation Failed",
        "Failed to generate commit message: %s",
        NotificationType.ERROR
    ),
    COMMIT_MESSAGE_GENERATED(
        "Commit Message Generated",
        "Your commit message has been successfully generated.",
        NotificationType.INFORMATION
    );

    fun getTitle(): String = title
    fun getContent(): String = content
    fun getType(): NotificationType = type

    fun show(project: Project?, vararg args: Any?) {
        val formattedContent = content.format(*args)
        NotificationService.show(project, title, formattedContent, type)
    }
}
