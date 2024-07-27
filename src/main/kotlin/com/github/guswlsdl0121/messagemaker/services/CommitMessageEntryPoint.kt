package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.services.commit.CommitService
import com.github.guswlsdl0121.messagemaker.services.diff.DiffService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class CommitMessageEntryPoint(
    private val commitService: CommitService,
    private val diffService: DiffService,
    private val logger: Logger = Logger.getInstance(CommitMessageEntryPoint::class.java)
) {
    fun run(e: AnActionEvent) {
        val workFlow = commitService.getWorkFlow(e)
            ?: error("CommitWorkflowHandler가 null입니다")

        val diff = diffService.getDiffInVcs(workFlow)
            .takeIf { it.isNotEmpty() } ?: error("선택된 변경 사항이 없습니다.")

        logger.info("생성된 커밋 메시지: \n$diff")
    }
}
