package com.github.guswlsdl0121.messagemaker.handler

import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class WorkFlowImpl(
    private val handler: AbstractCommitWorkflowHandler<*, *>
) : WorkFlow {
    override fun getIncludedChanges(): List<Change> {
        return handler.ui.getIncludedChanges()
    }
}
