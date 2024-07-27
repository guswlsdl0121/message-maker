package com.github.guswlsdl0121.messagemaker.prompt

import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision

object DiffPrompter {
    fun generate(changes: List<Change>): String {
        val detailedDiffs = changes.map { generateDetailedDiff(it) }
        val summary = summarizeChanges(changes)
        return buildString {
            append("# Content to be generated as commit message\n")
            append("## Summary of changes\n")
            append(summary)
            append("\n\n## Detailed changes by file\n")
            detailedDiffs.forEach {
                append(it)
                append("\n")
            }
        }
    }

    private fun summarizeChanges(changes: List<Change>): String {
        val added = changes.count { it.type == Change.Type.NEW }
        val deleted = changes.count { it.type == Change.Type.DELETED }
        val modified = changes.count { it.type == Change.Type.MODIFICATION }
        val moved = changes.count { it.type == Change.Type.MOVED }

        return buildString {
            append("- **Added files**: $added\n")
            append("- **Deleted files**: $deleted\n")
            append("- **Modified files**: $modified\n")
            append("- **Moved files**: $moved\n")
        }
    }

    private fun generateDetailedDiff(change: Change): String {
        val changeType = change.type
        val beforePath = change.beforeRevision?.file?.path
        val afterPath = change.afterRevision?.file?.path
        val diffContent = generateDiffContent(change)

        return buildString {
            append("### ${afterPath ?: beforePath}\n")
            append("**Change type**: $changeType\n")
            beforePath?.let { append("**Path before change**: $it\n") }
            afterPath?.let { append("**Path after change**: $it\n") }
            append("**Detailed changes**:\n")
            append("```\n")
            append(diffContent)
            append("\n```\n")
        }
    }

    private fun generateDiffContent(change: Change): String {
        val beforeContent = getContent(change.beforeRevision)
        val afterContent = getContent(change.afterRevision)
        return getUnifiedDiff(beforeContent, afterContent)
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
