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
        val changeTypeCounts = changes
            .groupBy { it.type }
            .mapValues { it.value.size }
        val detailedDiffs = changes.joinToString("\n") { change ->
            generateDetailedDiff(change, diffGenerator)
        }
        return """
# Content to be generated as commit message

## Summary of changes
- **Added files**: ${changeTypeCounts[Change.Type.NEW] ?: 0}
- **Deleted files**: ${changeTypeCounts[Change.Type.DELETED] ?: 0}
- **Modified files**: ${changeTypeCounts[Change.Type.MODIFICATION] ?: 0}
- **Moved files**: ${changeTypeCounts[Change.Type.MOVED] ?: 0}

## Detailed changes by file
$detailedDiffs""".trimIndent()
    }

    private fun generateDetailedDiff(change: Change, diffGenerator: DiffGenerator): String {
        val beforeName = change.beforeRevision?.file?.name
        val afterName = change.afterRevision?.file?.name

        val diffContent = diffGenerator.generate(
            change.beforeRevision?.content ?: "",
            change.afterRevision?.content ?: ""
        )
        return """
### ${afterName ?: beforeName}
**Change type**: ${change.type}
${beforeName?.let { "**Path before change**: $it\n" } ?: ""}${afterName?.let { "**Path after change**: $it\n" } ?: ""}
**Detailed changes**:
$diffContent
"""
    }
}
