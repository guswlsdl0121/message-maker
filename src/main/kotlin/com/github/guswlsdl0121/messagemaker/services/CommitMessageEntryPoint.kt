package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.services.diff.DiffSummaryService
import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.github.guswlsdl0121.messagemaker.services.vsc.CommitService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CommitMessageEntryPoint(private val project: Project) {
    fun run(e: AnActionEvent) {
        val commitService = project.service<CommitService>()
        val diffSummaryService = project.service<DiffSummaryService>()

        val changes = commitService.getIncludedChanges(e)
        if (changes.isNullOrEmpty()) {
            NotificationService.showWarning(project, "변경사항이 없습니다.")
            return
        }

        val diff = diffSummaryService.summaryDiff(changes)
        LOG.info("\n$diff")
    }

    companion object {
        private val LOG = logger<CommitMessageEntryPoint>()
    }
}
