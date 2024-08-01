package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.services.diff.generator.DiffGenerator
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change

@Service(Service.Level.PROJECT)
class DiffSummaryService(private val project: Project) {
    fun summaryDiff(changes: List<Change>): String {
        val diffGenerator = project.service<DiffGenerator>()
        val changeTypeCounts = mutableMapOf<Change.Type, Int>()

        val detailedChanges = changes.joinToString("\n") { change ->
            val type = refineChangeType(change)
            changeTypeCounts[type] = changeTypeCounts.getOrDefault(type, 0) + 1
            generateDetailedDiff(change, type, diffGenerator)
        }

        return buildString {
            appendLine("# Commit Message")
            appendLine()
            appendLine("## Summary of Changes")
            appendLine("- Added files: ${changeTypeCounts[Change.Type.NEW] ?: 0}")
            appendLine("- Deleted files: ${changeTypeCounts[Change.Type.DELETED] ?: 0}")
            appendLine("- Modified files: ${changeTypeCounts[Change.Type.MODIFICATION] ?: 0}")
            appendLine("- Moved files: ${changeTypeCounts[Change.Type.MOVED] ?: 0}")
            appendLine()
            appendLine("## Detailed Changes by File")
            append(detailedChanges)
        }
    }

    private fun refineChangeType(change: Change): Change.Type {
        if (change.type == Change.Type.MOVED) {
            val beforeContent = change.beforeRevision?.content ?: ""
            val afterContent = change.afterRevision?.content ?: ""
            if (beforeContent != afterContent) {
                return Change.Type.MODIFICATION
            }
        }
        return change.type
    }

    private fun generateDetailedDiff(change: Change, type: Change.Type, diffGenerator: DiffGenerator): String {
        val beforeName = change.beforeRevision?.file?.name
        val afterName = change.afterRevision?.file?.name
        val diffContent = diffGenerator.generate(
            change.beforeRevision?.content ?: "",
            change.afterRevision?.content ?: ""
        ).trimEnd()

        return buildString {
            appendLine("### ${afterName ?: beforeName}")
            appendLine("Change type: $type")
            beforeName?.let { appendLine("Path before change: $it") }
            afterName?.let { appendLine("Path after change: $it") }
            appendLine("Detailed changes:")
            appendLine(diffContent)
        }.trimEnd()
    }
}
