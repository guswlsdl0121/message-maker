package com.github.guswlsdl0121.messagemaker.services.vsc

import com.github.guswlsdl0121.messagemaker.exception.NoChangesException
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.vcs.commit.CommitWorkflowUi
import io.mockk.*

class CommitServiceTest : BasePlatformTestCase() {

    private lateinit var commitService: CommitService
    private lateinit var mockEvent: AnActionEvent
    private lateinit var mockHandler: AbstractCommitWorkflowHandler<*, *>
    private lateinit var mockUi: CommitWorkflowUi

    override fun setUp() {
        super.setUp()
        commitService = CommitService()
        mockEvent = mockk()
        mockHandler = mockk()
        mockUi = mockk()

        // Workflow handler 및 UI 설정
        every { mockHandler.ui } returns mockUi
        every { mockEvent.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) } returns mockHandler
    }

    fun testExistingChangesReturned() {
        val expectedChanges = listOf(mockk<Change>())
        every { mockUi.getIncludedChanges() } returns expectedChanges

        val result = commitService.getIncludedChanges(mockEvent)

        assertEquals("반환된 변경 사항이 기대한 변경 사항과 일치해야 합니다.", expectedChanges, result)
    }

    fun testExceptionThrownWhenNoChanges() {
        every { mockUi.getIncludedChanges() } returns emptyList()

        assertThrows(NoChangesException::class.java) {
            commitService.getIncludedChanges(mockEvent)
        }
    }

    fun testExceptionThrownWhenCommitWorkflowHandlerIsNull() {
        every { mockEvent.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER) } returns null

        assertThrows(NoChangesException::class.java) {
            commitService.getIncludedChanges(mockEvent)
        }
    }

}
