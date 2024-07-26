package com.github.guswlsdl0121.messagemaker.services

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.openapi.vcs.VcsDataKeys

class CommitMessageService(e: AnActionEvent) {
    private val logger = Logger.getInstance(CommitMessageService::class.java)
    private val commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>? = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? AbstractCommitWorkflowHandler<*, *>
    private val diffProvider: DiffProviderInVcs? = commitWorkflowHandler?.let { DiffProviderInVcs(it) }

    fun getDiff(): String {
        if (commitWorkflowHandler == null) {
            logger.warn("CommitWorkflowHandler is null")
            return ""
        }
        return diffProvider?.getDiff() ?: ""
    }

    fun generateCommitMessage(diff: String) {
        if (diff.isNotEmpty()) {
            logger.info("Generated commit message from diff: $diff")
        }
    }
}
