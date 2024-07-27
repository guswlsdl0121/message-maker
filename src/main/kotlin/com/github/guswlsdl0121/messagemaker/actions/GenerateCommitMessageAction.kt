package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.providers.CommitWorkflowHandlerProviderImpl
import com.github.guswlsdl0121.messagemaker.providers.DiffProviderImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.github.guswlsdl0121.messagemaker.services.CommitMessageService

class GenerateCommitMessageAction : AnAction() {
    private val logger = Logger.getInstance(GenerateCommitMessageAction::class.java)
    private val commitMessageService = CommitMessageService(
        CommitWorkflowHandlerProviderImpl(),
        DiffProviderImpl()
    )

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            commitMessageService.generateCommitMessage(e)
        } ?: run {
            logger.warn("프로젝트가 null입니다")
        }
    }
}
