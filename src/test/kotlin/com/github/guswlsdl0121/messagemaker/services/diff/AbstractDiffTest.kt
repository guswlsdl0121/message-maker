package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.utils.FileUtils
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.LocalFilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision
import com.intellij.openapi.vcs.history.VcsRevisionNumber
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase


abstract class AbstractDiffTest : BasePlatformTestCase() {

    abstract fun getRelativeTestDataPath(): String

    override fun getTestDataPath(): String {
        return "src/test/testData/" + getRelativeTestDataPath()
    }

    override fun setUp() {
        super.setUp()
        myFixture.copyDirectoryToProject(".", "")
    }

    protected fun prepareChange(testName: String): Change {
        val beforeFilePath = "$testName/before.txt"
        val afterFilePath = "$testName/after.txt"

        return when {
            fileExistsInProject(beforeFilePath) && fileExistsInProject(afterFilePath) -> {
                val oldFile = myFixture.copyFileToProject(beforeFilePath)
                val newFile = myFixture.copyFileToProject(afterFilePath)
                createChange(oldFile, newFile)
            }
            fileExistsInProject(afterFilePath) -> {
                val newFile = myFixture.copyFileToProject(afterFilePath)
                createChange(null, newFile)
            }
            fileExistsInProject(beforeFilePath) -> {
                val oldFile = myFixture.copyFileToProject(beforeFilePath)
                createChange(oldFile, null)
            }
            else -> throw IllegalStateException("Both before.txt and after.txt are missing for test: $testName")
        }
    }

    protected fun prepareChange(testName: String, additionalContent: String): Change {
        val beforeFilePath = "$testName/before.txt"

        if (!fileExistsInProject(beforeFilePath)) {
            throw IllegalStateException("before.txt is missing for test: $testName")
        }

        val beforeFile = myFixture.copyFileToProject(beforeFilePath)
        val beforeContent = getFileContent(beforeFile)

        val modifiedFile = myFixture.copyFileToProject(beforeFilePath, beforeFilePath)

        FileUtils.modifyFile(project, modifiedFile, additionalContent)

        VirtualFileManager.getInstance().syncRefresh()

        val virtualBeforeFile =
            FileUtils.createVirtualFile(modifiedFile.name, beforeContent, modifiedFile.path, modifiedFile.url)

        return createChange(virtualBeforeFile, modifiedFile)
    }

    protected fun verifyResult(testName: String, summary: String) {
        print("$testName/expected.txt")
        val expected = myFixture.configureByFile("$testName/expected.txt").text
        assertEquals(expected.trim(), summary.trim())
    }

    private fun createChange(before: VirtualFile?, after: VirtualFile?): Change {
        val beforeRevision = before?.let { createContentRevision(it) }
        val afterRevision = after?.let { createContentRevision(it) }
        val fileStatus = when {
            beforeRevision == null -> FileStatus.ADDED
            afterRevision == null -> FileStatus.DELETED
            else -> FileStatus.MODIFIED
        }
        return Change(beforeRevision, afterRevision, fileStatus)
    }

    private fun createContentRevision(file: VirtualFile): ContentRevision {
        return object : ContentRevision {
            override fun getContent(): String = getFileContent(file).trimEnd()
            override fun getFile(): FilePath = LocalFilePath(file.path, file.isDirectory)
            override fun getRevisionNumber(): VcsRevisionNumber = VcsRevisionNumber.NULL
        }
    }

    private fun fileExistsInProject(path: String): Boolean {
        return myFixture.tempDirFixture.getFile(path) != null
    }

    private fun getFileContent(file: VirtualFile): String {
        return file.contentsToByteArray().toString(Charsets.UTF_8)
    }
}
