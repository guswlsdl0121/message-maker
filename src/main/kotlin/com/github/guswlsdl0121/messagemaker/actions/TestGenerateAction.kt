package com.github.guswlsdl0121.messagemaker.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class TestGenerateAction : DumbAwareAction() {
    private val logger = Logger.getInstance(TestGenerateAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        e.project!!

        val commitWorkflowHandler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as AbstractCommitWorkflowHandler<*, *>
        val includedChanges: List<Change> = commitWorkflowHandler.ui.getIncludedChanges()


        if (includedChanges.isEmpty()) {
            logger.info("No changes selected")
            return
        }

        val diff = buildString {
            includedChanges.forEach { change ->
                val beforePath = change.beforeRevision?.file?.path ?: "unknown"
                val afterPath = change.afterRevision?.file?.path ?: "unknown"
                logger.info("Change: $beforePath -> $afterPath")
                append("Change: $beforePath -> $afterPath\n")
            }
        }

        logger.info("Generated commit message from diff:\n $diff")
    }
}
