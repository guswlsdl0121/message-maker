package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationType

enum class Notification(
    val title: String,
    val content: String,
    val type: NotificationType
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
}
