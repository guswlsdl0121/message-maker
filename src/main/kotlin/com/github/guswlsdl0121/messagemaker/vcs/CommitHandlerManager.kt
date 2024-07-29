package com.github.guswlsdl0121.messagemaker.vcs

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

typealias CommitWorkflowHandler = AbstractCommitWorkflowHandler<*, *>

@Service(Service.Level.PROJECT)
class CommitHandlerManager {
    private var handler: CommitWorkflowHandler? = null

    fun updateHandler(e: AnActionEvent) {
        handler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? CommitWorkflowHandler
    }

    fun getIncludedChanges(): List<Change>? = handler?.ui?.getIncludedChanges()
}
