package com.github.guswlsdl0121.messagemaker.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class DataKeyDebugAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        // 여기에 중단점을 설정
        val project: Project? = e.getData(CommonDataKeys.PROJECT)

        if (project != null) {
            val message = "Project name: ${project.name}"
            Messages.showInfoMessage(project, message, "DataKey Debug Info")
        } else {
            Messages.showInfoMessage("No project found", "DataKey Debug Info")
        }
    }
}
