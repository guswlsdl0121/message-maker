package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.exception.ActionExceptionHandler
import com.github.guswlsdl0121.messagemaker.exception.ProjectNullException
import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction

class GenerateCommitMessageAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        try {
            e.project?.service<CommitMessageEntryPoint>()
                ?.run(e)
                ?: throw ProjectNullException()
        } catch (ex: Exception) {
            ActionExceptionHandler.handle(e.project, ex)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
