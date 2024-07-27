package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision

object DiffUtils {
    fun generateDiff(change: Change): String {
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

