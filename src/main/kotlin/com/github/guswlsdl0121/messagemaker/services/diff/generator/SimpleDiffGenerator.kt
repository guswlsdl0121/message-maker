package com.github.guswlsdl0121.messagemaker.services.diff.generator

import com.intellij.openapi.vcs.changes.Change


class SimpleDiffGenerator : DiffGenerator {
    override fun generate(changes: List<Change>): String {
        return changes.joinToString("\n") { change ->
            generateSingleChange(change)
        }.trimEnd()
    }

    private fun generateSingleChange(change: Change): String {
        val beforeContent = change.beforeRevision?.content ?: ""
        val beforeName = change.beforeRevision?.file?.name
        val afterContent = change.afterRevision?.content ?: ""
        val afterName = change.afterRevision?.file?.name

        val changeType = change.type
        val diffContent = generateDiff(beforeContent, afterContent)

        return buildString {
            appendLine("### ${afterName ?: beforeName}")
            appendLine("Change type: $changeType")
            beforeName?.let { appendLine("Path before change: $it") }
            afterName?.let { appendLine("Path after change: $it") }
            appendLine("Detailed changes:")
            appendLine(diffContent)
        }.trimEnd()
    }

    private fun generateDiff(beforeContent: String, afterContent: String): String {
        val beforeLines = beforeContent.lines()
        val afterLines = afterContent.lines()

        return buildString {
            var i = 0
            var j = 0

            while (i < beforeLines.size || j < afterLines.size) {
                when {
                    i >= beforeLines.size -> append("+${afterLines[j++]}\n")
                    j >= afterLines.size -> append("-${beforeLines[i++]}\n")
                    beforeLines[i] == afterLines[j] -> {
                        i++
                        j++
                    }

                    else -> {
                        if (beforeLines[i].isNotEmpty()) append("-${beforeLines[i]}\n")
                        if (afterLines[j].isNotEmpty()) append("+${afterLines[j]}\n")
                        i++
                        j++
                    }
                }
            }
        }.trimEnd()
    }
}
