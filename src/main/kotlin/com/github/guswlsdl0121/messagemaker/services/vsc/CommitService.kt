package com.github.guswlsdl0121.messagemaker.services.vsc

import com.github.guswlsdl0121.messagemaker.exception.NoChangesException
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

typealias CommitWorkflowHandler = AbstractCommitWorkflowHandler<*, *>

@Service(Service.Level.PROJECT)
class CommitService {
    private var handler: CommitWorkflowHandler? = null

    fun getIncludedChanges(e: AnActionEvent): List<Change> {
        updateHandler(e)

        return handler?.ui?.getIncludedChanges()
            .takeIf { !it.isNullOrEmpty() }
            ?: throw NoChangesException()
    }

    private fun updateHandler(e: AnActionEvent) {
        handler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? CommitWorkflowHandler
    }
}
