package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.providers.CommitWorkflowHandlerProvider
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class CommitMessageEntryPoint(
    private val commitWorkflowHandlerProvider: CommitWorkflowHandlerProvider,
    private val diffService: DiffService
) {
    private val logger = Logger.getInstance(CommitMessageEntryPoint::class.java)

    fun run(e: AnActionEvent) {
        val commitWorkflowHandler = commitWorkflowHandlerProvider.getCommitWorkflowHandler(e)
            ?: error("CommitWorkflowHandler가 null입니다")

        val diff = diffService.getDiffInVcs(commitWorkflowHandler)
            .takeIf { it.isNotEmpty() } ?: error("선택된 변경 사항이 없습니다.")

        logger.info("생성된 커밋 메시지: \n$diff")
    }
}
