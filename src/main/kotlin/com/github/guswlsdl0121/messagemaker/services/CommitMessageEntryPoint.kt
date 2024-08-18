package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.services.diff.DiffSummaryGenerator
import com.github.guswlsdl0121.messagemaker.services.vsc.CommitHandler
import com.github.guswlsdl0121.messagemaker.utils.PluginLogger
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CommitMessageEntryPoint(private val project: Project) {
    fun run(e: AnActionEvent) {
        val commitHandler = project.service<CommitHandler>()
        val diffSummaryGenerator = project.service<DiffSummaryGenerator>()

        val changes = commitHandler.getCheckedChanges(e)
        val diffSummary = diffSummaryGenerator.getDiff(changes)

        PluginLogger.info("\n$diffSummary")
    }
}
