package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.prompt.DiffPrompter
import com.github.guswlsdl0121.messagemaker.services.commit.CommitServiceImpl
import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.github.guswlsdl0121.messagemaker.services.diff.DefaultDiffService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class GenerateCommitMessageAction(
    private val commitMessageEntryPoint: CommitMessageEntryPoint = CommitMessageEntryPoint(
        CommitServiceImpl(),
        DefaultDiffService(DiffPrompter),
        Logger.getInstance(CommitMessageEntryPoint::class.java)
    )
) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: error("프로젝트가 null입니다")
        commitMessageEntryPoint.run(e)
    }
}
