package com.github.guswlsdl0121.messagemaker.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.github.guswlsdl0121.messagemaker.services.CommitMessageService

class GenerateCommitMessageAction : AnAction() {
    private val logger = Logger.getInstance(GenerateCommitMessageAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        e.project ?: run {
            logger.warn("Project is null")
            return
        }

        val commitMessageService = CommitMessageService(e)
        val diff = commitMessageService.getDiff()

        if (diff.isNotEmpty()) {
            commitMessageService.generateCommitMessage(diff)
        }
    }
}

