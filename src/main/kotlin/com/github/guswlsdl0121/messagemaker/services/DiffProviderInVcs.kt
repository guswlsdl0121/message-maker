package com.github.guswlsdl0121.messagemaker.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.openapi.vcs.changes.Change


class DiffProviderInVcs(commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>) {
    private val logger = Logger.getInstance(DiffProviderInVcs::class.java)
    private val includedChanges: List<Change> = commitWorkflowHandler.ui.getIncludedChanges()

    fun getDiff(): String {
        return if (includedChanges.isEmpty()) {
            logger.info("No changes selected")
            ""
        } else {
            buildString {
                includedChanges.forEach { change ->
                    val beforePath = change.beforeRevision?.file?.path ?: "unknown"
                    val afterPath = change.afterRevision?.file?.path ?: "unknown"
                    logger.info("Change: $beforePath -> $afterPath")
                    append("Change: $beforePath -> $afterPath\n")
                }
            }
        }
    }
}
