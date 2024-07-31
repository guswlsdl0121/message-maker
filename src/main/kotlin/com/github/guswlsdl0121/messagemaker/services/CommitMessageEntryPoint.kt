package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.services.diff.DiffSummaryService
import com.github.guswlsdl0121.messagemaker.services.vsc.CommitService
import com.github.guswlsdl0121.messagemaker.utils.LogFactory
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CommitMessageEntryPoint(private val project: Project) {
    fun run(e: AnActionEvent) {
        val commitService = project.service<CommitService>()
        val diffSummaryService = project.service<DiffSummaryService>()

        val changes = commitService.getIncludedChanges(e)
        val diff = diffSummaryService.summaryDiff(changes)

        LogFactory.info("\n$diff")
    }
}
