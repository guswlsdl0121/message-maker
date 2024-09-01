package com.github.guswlsdl0121.messagemaker.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys

class DataKeyDebugAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        // 여기에 중단점을 설정
        val handler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER)
        println(handler)
    }
}
