package com.github.guswlsdl0121.messagemaker.services.commit

import com.github.guswlsdl0121.messagemaker.handler.WorkFlow
import com.intellij.openapi.actionSystem.AnActionEvent

interface CommitService {
    fun getWorkFlow(e: AnActionEvent): WorkFlow?
}
