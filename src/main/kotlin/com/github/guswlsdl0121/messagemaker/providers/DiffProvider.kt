package com.github.guswlsdl0121.messagemaker.providers

import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

interface DiffProvider {
    fun getDiff(commitWorkflowHandler: AbstractCommitWorkflowHandler<*, *>): String
}
