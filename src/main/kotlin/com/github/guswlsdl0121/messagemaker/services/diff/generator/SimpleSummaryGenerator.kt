package com.github.guswlsdl0121.messagemaker.services.diff.generator

import com.github.guswlsdl0121.messagemaker.utils.ChangeTypeUtil
import com.intellij.openapi.vcs.changes.Change

class SimpleSummaryGenerator : SummaryGenerator {
    override fun generate(changes: List<Change>): String {
        val changeTypeCounts = ChangeTypeUtil.countChangeTypes(changes)

        return buildString {
            appendLine("# Summary of Changes")
            appendLine("- Added files: ${changeTypeCounts[Change.Type.NEW] ?: 0}")
            appendLine("- Deleted files: ${changeTypeCounts[Change.Type.DELETED] ?: 0}")
            appendLine("- Modified files: ${changeTypeCounts[Change.Type.MODIFICATION] ?: 0}")
            appendLine("- Moved files: ${changeTypeCounts[Change.Type.MOVED] ?: 0}")
        }.trimEnd()
    }
}
