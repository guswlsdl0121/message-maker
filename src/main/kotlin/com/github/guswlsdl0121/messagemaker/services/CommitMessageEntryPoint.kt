package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.services.diff.DiffService
import com.github.guswlsdl0121.messagemaker.services.vsc.CommitService
import com.github.guswlsdl0121.messagemaker.utils.PluginLogger
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CommitMessageEntryPoint(private val project: Project) {
    fun run(event: AnActionEvent) {
        val commitService = project.service<CommitService>()
        val diffService = project.service<DiffService>()

        val changes = commitService.getCheckedChanges(event)
        val diff = diffService.getDiff(changes)

        PluginLogger.info("\n$diff")
    }
}
