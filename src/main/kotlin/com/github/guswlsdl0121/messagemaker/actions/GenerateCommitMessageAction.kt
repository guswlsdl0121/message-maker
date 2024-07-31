package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.github.guswlsdl0121.messagemaker.utils.ActionUtils.executeWithErrorHandling
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction

class GenerateCommitMessageAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.executeWithErrorHandling { project ->
            project.service<CommitMessageEntryPoint>().run(e)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
