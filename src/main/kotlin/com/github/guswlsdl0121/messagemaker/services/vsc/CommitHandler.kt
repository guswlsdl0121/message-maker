package com.github.guswlsdl0121.messagemaker.services.vsc

import com.github.guswlsdl0121.messagemaker.exception.NoChangesException
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

typealias CommitWorkflowHandler = AbstractCommitWorkflowHandler<*, *>

@Service(Service.Level.PROJECT)
class CommitHandler {
    fun getCheckedChanges(e: AnActionEvent): List<Change> {
        val handler = getHandler(e) ?: return emptyList()

        return handler.ui.getIncludedChanges()
            .takeIf { it.isNotEmpty() }
            ?: throw NoChangesException()
    }

    private fun getHandler(e: AnActionEvent): CommitWorkflowHandler? {
        return e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? CommitWorkflowHandler
    }
}
