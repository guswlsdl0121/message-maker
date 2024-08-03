package com.github.guswlsdl0121.messagemaker.services.vsc

import com.github.guswlsdl0121.messagemaker.exception.NoChangesException
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.vcs.commit.CommitWorkflowUi
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*

class CommitServiceTest {

    private lateinit var commitService: CommitService
    private lateinit var mockEvent: AnActionEvent
    private lateinit var mockHandler: AbstractCommitWorkflowHandler<*, *>
    private lateinit var mockUi: CommitWorkflowUi

    @Before
    fun setUp() {
        commitService = CommitService()
        mockEvent = mock(AnActionEvent::class.java)
        mockHandler = mock(AbstractCommitWorkflowHandler::class.java)
        mockUi = mock(CommitWorkflowUi::class.java)

        // Workflow handler 및 UI 설정
        `when`(mockHandler.ui).thenReturn(mockUi)
        `when`(mockEvent.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER)).thenReturn(mockHandler)
    }

    @Test
    fun `존재하는 변경사항에 대한  반환 확인`() {
        val expectedChanges = listOf(mock(Change::class.java))
        `when`(mockUi.getIncludedChanges()).thenReturn(expectedChanges)

        val result = commitService.getIncludedChanges(mockEvent)

        assertEquals("반환된 변경 사항이 기대한 변경 사항과 일치해야 합니다.", expectedChanges, result)
    }

    @Test(expected = NoChangesException::class)
    fun `변경 사항이 없을 때 예외 발생 확인`() {
        `when`(mockUi.getIncludedChanges()).thenReturn(emptyList())

        commitService.getIncludedChanges(mockEvent)
    }

    @Test(expected = NoChangesException::class)
    fun `UI가 null일 때 예외 처리 확인`() {
        `when`(mockHandler.ui).thenReturn(null)

        commitService.getIncludedChanges(mockEvent)
    }
}
