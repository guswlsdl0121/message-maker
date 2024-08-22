package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.services.diff.constants.DiffConstants
import com.github.guswlsdl0121.messagemaker.services.diff.generator.DiffDetailGenerator
import com.github.guswlsdl0121.messagemaker.services.diff.generator.DiffSummaryGenerator
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change

@Service(Service.Level.PROJECT)
class DiffService(private val project: Project) {
    fun getDiff(changes: List<Change>): String {
        val summary = project.service<DiffSummaryGenerator>().generate(changes)
        val detailedChanges = project.service<DiffDetailGenerator>().generate(changes)

        return buildString {
            appendLine(DiffConstants.DEFAULT_TITLE)
            appendLine()
            appendLine(summary)
            appendLine()
            appendLine(detailedChanges)
            appendLine()
            appendLine(DiffConstants.ADDITIONAL_NOTES)
        }.trimEnd()
    }
}
