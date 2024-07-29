package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.services.CommitService
import com.github.guswlsdl0121.messagemaker.vcs.CommitHandlerManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAwareAction

class GenerateCommitMessageAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: run {
            LOG.error("프로젝트가 null입니다")
            return
        }

        val commitHandlerManager = project.service<CommitHandlerManager>()
        commitHandlerManager.updateHandler(e)

        val commitService = project.service<CommitService>()
        val diff = commitService.extractDiff()

        // TODO: diff를 사용하여 추가 작업 수행 (예: 사용자에게 보여주기)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    companion object {
        private val LOG = logger<GenerateCommitMessageAction>()
    }
}
