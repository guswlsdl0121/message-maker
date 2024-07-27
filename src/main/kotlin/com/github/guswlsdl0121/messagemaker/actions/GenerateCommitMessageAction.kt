package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.prompt.DiffPrompter
import com.github.guswlsdl0121.messagemaker.providers.CommitWorkflowHandlerProviderImpl
import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.github.guswlsdl0121.messagemaker.services.DiffService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class GenerateCommitMessageAction : AnAction() {
    private val commitMessageEntryPoint = CommitMessageEntryPoint(
        CommitWorkflowHandlerProviderImpl(),
        DiffService(DiffPrompter)
    )

    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: error("프로젝트가 null입니다")
        commitMessageEntryPoint.run(e)
    }
}
