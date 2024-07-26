package com.github.guswlsdl0121.messagemaker.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision

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
                    val diff = generateDiff(change)
                    append(diff)
                }
            }
        }
    }

    private fun generateDiff(change: Change): String {
        val beforeContent = getContent(change.beforeRevision)
        val afterContent = getContent(change.afterRevision)
        return buildString {
            append("diff --git a/${change.beforeRevision?.file?.path} b/${change.afterRevision?.file?.path}\n")
            append("--- a/${change.beforeRevision?.file?.path}\n")
            append("+++ b/${change.afterRevision?.file?.path}\n")
            append(getUnifiedDiff(beforeContent, afterContent))
        }
    }

    private fun getContent(revision: ContentRevision?): String {
        return revision?.content ?: ""
    }

    private fun getUnifiedDiff(beforeContent: String, afterContent: String): String {
        val beforeLines = beforeContent.lines()
        val afterLines = afterContent.lines()
        val diffs = mutableListOf<String>()
        var commonPrefix = 0
        while (commonPrefix < beforeLines.size && commonPrefix < afterLines.size && beforeLines[commonPrefix] == afterLines[commonPrefix]) {
            commonPrefix++
        }
        val commonSuffix = minOf(beforeLines.size, afterLines.size) - commonPrefix
        val beforeDiff = beforeLines.drop(commonPrefix).dropLast(commonSuffix)
        val afterDiff = afterLines.drop(commonPrefix).dropLast(commonSuffix)
        diffs.addAll(beforeDiff.map { "-$it" })
        diffs.addAll(afterDiff.map { "+$it" })
        return diffs.joinToString("\n")
    }
}
