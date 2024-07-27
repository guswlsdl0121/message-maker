package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.providers.CommitWorkflowHandlerProvider
import com.github.guswlsdl0121.messagemaker.providers.DiffProvider
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class CommitMessageService(
    private val commitWorkflowHandlerProvider: CommitWorkflowHandlerProvider,
    private val diffProvider: DiffProvider
) {
    private val logger = Logger.getInstance(CommitMessageService::class.java)

    fun generateCommitMessage(e: AnActionEvent) {
        val commitWorkflowHandler = commitWorkflowHandlerProvider.getCommitWorkflowHandler(e) ?: run {
            logger.warn("CommitWorkflowHandler가 null입니다")
            return
        }

        val diff = diffProvider.getDiff(commitWorkflowHandler)
        if (diff.isNotEmpty()) {
            logger.info("diff에서 생성된 커밋 메시지: $diff")
        }
    }
}
