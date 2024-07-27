package com.github.guswlsdl0121.messagemaker.providers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class CommitWorkflowHandlerProviderImpl : CommitWorkflowHandlerProvider {
    override fun getCommitWorkflowHandler(e: AnActionEvent): AbstractCommitWorkflowHandler<*, *>? {
        return e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? AbstractCommitWorkflowHandler<*, *>
    }
}
