package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAwareAction

class GenerateCommitMessageAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.service<CommitMessageEntryPoint>()
            ?.run(e)
            ?: LOG.error("프로젝트가 null입니다")
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    companion object {
        private val LOG = logger<GenerateCommitMessageAction>()
    }
}
