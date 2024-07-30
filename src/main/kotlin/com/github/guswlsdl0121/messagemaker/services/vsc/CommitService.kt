package com.github.guswlsdl0121.messagemaker.services.vsc

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler

typealias CommitWorkflowHandler = AbstractCommitWorkflowHandler<*, *>

@Service(Service.Level.PROJECT)
class CommitService {
    private var handler: CommitWorkflowHandler? = null

    fun getIncludedChanges(e: AnActionEvent): List<Change>? {
        updateHandler(e)
        val changes = handler?.ui?.getIncludedChanges()
        if (changes.isNullOrEmpty()) {
            LOG.warn("변경사항이 없거나 CommitWorkflowHandler가 설정되지 않았습니다.")
            return null
        }
        return changes
    }

    private fun updateHandler(e: AnActionEvent) {
        val newHandler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) as? CommitWorkflowHandler
        if (newHandler != handler) {
            handler = newHandler
            LOG.debug("CommitWorkflowHandler가 업데이트되었습니다.")
        }
    }

    companion object {
        private val LOG = logger<CommitService>()
    }
}
