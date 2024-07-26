package com.github.guswlsdl0121.messagemaker.services

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.openapi.vcs.VcsDataKeys

class CommitMessageService {
    private val logger = Logger.getInstance(CommitMessageService::class.java)

    fun generateCommitMessage(e: AnActionEvent) {
        e.project ?: run {
            logger.warn("Project is null")
            return
        }

        val commitWorkflowHandler = getCommitWorkflowHandler(e) ?: run {
            logger.warn("CommitWorkflowHandler is null")
            return
        }

        val diffProvider = DiffProviderInVcs(commitWorkflowHandler)
        val diff = diffProvider.getDiff()
        if (diff.isNotEmpty()) {
            logger.info("Generated commit message from diff: $diff")
        }
    }

    private fun getCommitWorkflowHandler(e: AnActionEvent): AbstractCommitWorkflowHandler<*, *>? {
        return e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? AbstractCommitWorkflowHandler<*, *>
    }
}
