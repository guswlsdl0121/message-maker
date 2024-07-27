package com.github.guswlsdl0121.messagemaker.providers

import com.github.guswlsdl0121.messagemaker.utils.DiffUtils
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class DiffProviderImpl : DiffProvider {
    private val logger = Logger.getInstance(DiffProviderImpl::class.java)

    override fun getDiff(commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>): String {
        val includedChanges: List<Change> = commitWorkflowHandler.ui.getIncludedChanges()
        return if (includedChanges.isEmpty()) {
            logger.info("No changes selected")
            ""
        } else {
            buildString {
                includedChanges.forEach { change ->
                    val diff = DiffUtils.generateDiff(change)
                    append(diff)
                }
            }
        }
    }
}
