package com.github.guswlsdl0121.messagemaker.services.commit

import com.github.guswlsdl0121.messagemaker.handler.WorkFlow
import com.github.guswlsdl0121.messagemaker.handler.WorkFlowImpl
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

class CommitServiceImpl : CommitService {
    override fun getWorkFlow(e: AnActionEvent): WorkFlow? {
        val handler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? AbstractCommitWorkflowHandler<*, *>
        return handler?.let { WorkFlowImpl(it) }
    }
}
