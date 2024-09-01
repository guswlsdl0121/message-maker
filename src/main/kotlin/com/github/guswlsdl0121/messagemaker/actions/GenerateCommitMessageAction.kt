package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.exception.ActionExceptionHandler
import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction

class GenerateCommitMessageAction : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!

        try {
            project.service<CommitMessageEntryPoint>().run(event)
        } catch (ex: Exception) {
            project.service<ActionExceptionHandler>().handle(ex)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
