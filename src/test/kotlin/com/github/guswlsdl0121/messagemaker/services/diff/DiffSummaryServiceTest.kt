package com.github.guswlsdl0121.messagemaker.services.diff

import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.LocalFilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ContentRevision
import com.intellij.openapi.vcs.history.VcsRevisionNumber
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class DiffSummaryServiceTest : BasePlatformTestCase() {

    private lateinit var diffSummaryService: DiffSummaryService

    override fun getTestDataPath(): String {
        // 정확한 상대 경로 설정
        return "src/test/testData/diffSummary"
    }

    override fun setUp() {
        super.setUp()
        // 서비스 초기화 및 테스트 데이터 로드
        diffSummaryService = DiffSummaryService(project)
        // 테스트 데이터 디렉토리 미리 로드
        myFixture.copyDirectoryToProject(".", "")
    }

    fun test_파일변경() {
        doTest("fileModificationTest")
    }

    fun test_파일추가() {
        doTest("fileAdditionTest")
    }

    fun test_파일삭제() {
        doTest("fileDeletionTest")
    }

    private fun doTest(testName: String) {
        // 가상 파일 시스템에서 파일 접근
        val beforeFile = myFixture.findFileInTempDir("$testName/before.txt")
        val afterFile = myFixture.findFileInTempDir("$testName/after.txt")

        val change = when {
            beforeFile == null && afterFile != null -> {
                // 파일 추가 시나리오
                val newFile = myFixture.copyFileToProject("$testName/after.txt")
                createChange(null, newFile)
            }

            afterFile == null && beforeFile != null -> {
                // 파일 삭제 시나리오
                val deletedFile = myFixture.copyFileToProject("$testName/before.txt")
                createChange(deletedFile, null)
            }

            else -> {
                // 파일 수정 시나리오
                val oldFile = myFixture.copyFileToProject("$testName/before.txt")
                val newFile = myFixture.copyFileToProject("$testName/after.txt")
                createChange(oldFile, newFile)
            }
        }

        val summary = diffSummaryService.summaryDiff(listOf(change))

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
            override fun getContent(): String = String(file.contentsToByteArray(), Charsets.UTF_8).trimEnd()
            override fun getFile(): FilePath = LocalFilePath(file.path, file.isDirectory)
            override fun getRevisionNumber(): VcsRevisionNumber = VcsRevisionNumber.NULL
        }
    }
}
