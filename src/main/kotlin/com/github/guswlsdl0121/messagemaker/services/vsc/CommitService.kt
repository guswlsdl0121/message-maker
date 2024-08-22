package com.github.guswlsdl0121.messagemaker.services.vsc

import com.github.guswlsdl0121.messagemaker.exception.NoChangesException
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

@Service(Service.Level.PROJECT)
class CommitService {
    fun getCheckedChanges(event: AnActionEvent): List<Change> {
        val handler = getHandler(event) ?: return emptyList()

        return handler.ui.getIncludedChanges()
            .takeIf { it.isNotEmpty() }
            ?: throw NoChangesException()
    }

    private fun getHandler(event: AnActionEvent) =
        event.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? AbstractCommitWorkflowHandler<*, *>
}
