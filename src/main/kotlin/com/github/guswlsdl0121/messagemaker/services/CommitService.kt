package com.github.guswlsdl0121.messagemaker.services

import com.github.guswlsdl0121.messagemaker.prompt.DiffSummaryGenerator
import com.github.guswlsdl0121.messagemaker.vcs.CommitHandlerManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class CommitService(private val project: Project) {
    fun extractDiff(): String? {
        val commitHandlerManager = project.service<CommitHandlerManager>()
        val diffSummaryGenerator = project.service<DiffSummaryGenerator>()

        val includedChanges = commitHandlerManager.getIncludedChanges()
        if (includedChanges.isNullOrEmpty()) {
            LOG.warn("변경사항이 없거나 CommitWorkflowHandler가 설정되지 않았습니다.")
            return null
        }

        val diff = diffSummaryGenerator.generate(includedChanges)
        LOG.info("추출된 Diff: \n$diff")
        return diff
    }

    companion object {
        private val LOG = logger<CommitService>()
    }
}
