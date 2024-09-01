package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationType

enum class CommitMessageNotification(
    val title: String,
    val content: String,
    val type: NotificationType
) {
    NO_CHANGES_DETECTED(
        "No Changes Detected",
        "There are no changes to generate a commit message for.",
        NotificationType.WARNING
    ),
    UNEXPECTED_ERROR(
        "Unexpected Error",
        "An unexpected error occurred. Please check the logs for more details.",
        NotificationType.ERROR
    ),
    COMMIT_MESSAGE_GENERATED(
        "Commit Message Generated",
        "Your commit message has been successfully generated.",
        NotificationType.INFORMATION
    );
}
