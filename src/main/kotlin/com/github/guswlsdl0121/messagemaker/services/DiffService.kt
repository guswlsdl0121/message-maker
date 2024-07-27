package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.prompt.DiffPrompter
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class DiffService(private val diffPrompter: DiffPrompter) {
    fun getDiffInVcs(commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>): String {
        val includedChanges = getIncludedChanges(commitWorkflowHandler)
            .takeIf { it.isNotEmpty() } ?: error("선택된 변경 사항이 없습니다.")

        return diffPrompter.generate(includedChanges)
    }

    private fun getIncludedChanges(commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>): List<Change> {
        return commitWorkflowHandler.ui.getIncludedChanges()
    }
}
