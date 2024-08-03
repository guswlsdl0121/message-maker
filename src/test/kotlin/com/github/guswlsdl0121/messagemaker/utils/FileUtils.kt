package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.LightVirtualFile

object FileUtils {
    fun modifyFile(project: Project, file: VirtualFile, additionalContent: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            val document = FileDocumentManager.getInstance().getDocument(file)
            document?.let {
                it.insertString(it.textLength, additionalContent)
                FileDocumentManager.getInstance().saveDocument(it)
            }
            if (document != null) {
                PsiDocumentManager.getInstance(project).commitDocument(document)
            }
        }
    }

    fun createVirtualFile(name: String, content: String, path: String, url: String): VirtualFile {
        return object : LightVirtualFile(name, content) {
            override fun getPath(): String = path
            override fun getUrl(): String = url
        }
    }
}
