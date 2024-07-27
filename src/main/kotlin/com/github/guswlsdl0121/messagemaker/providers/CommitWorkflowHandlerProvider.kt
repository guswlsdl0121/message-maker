package com.github.guswlsdl0121.messagemaker.providers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

interface CommitWorkflowHandlerProvider {
    fun getCommitWorkflowHandler(e: AnActionEvent): AbstractCommitWorkflowHandler<*, *>?
}
