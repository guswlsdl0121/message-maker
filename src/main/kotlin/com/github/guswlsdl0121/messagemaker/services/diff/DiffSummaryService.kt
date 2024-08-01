package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.services.diff.generator.DiffGenerator
import com.github.guswlsdl0121.messagemaker.services.diff.generator.SummaryGenerator
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change

@Service(Service.Level.PROJECT)
class DiffSummaryService(private val project: Project) {
    fun summaryDiff(changes: List<Change>): String {
        val diffGenerator = project.service<DiffGenerator>()
        val summaryGenerator = project.service<SummaryGenerator>()

        val detailedChanges = diffGenerator.generate(changes)
        val summary = summaryGenerator.generate(changes)

        return buildString {
            appendLine(summary)
            append(detailedChanges)
        }
    }
}
