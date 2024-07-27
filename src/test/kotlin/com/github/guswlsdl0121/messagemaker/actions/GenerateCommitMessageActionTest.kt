package com.github.guswlsdl0121.messagemaker.actions

import com.github.guswlsdl0121.messagemaker.handler.WorkFlow
import com.github.guswlsdl0121.messagemaker.services.CommitMessageEntryPoint
import com.github.guswlsdl0121.messagemaker.services.commit.CommitService
import com.github.guswlsdl0121.messagemaker.services.diff.DiffService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito

@DisplayName("GenerateCommitMessageAction 테스트")
class GenerateCommitMessageActionTest : BasePlatformTestCase() {

    private lateinit var mockCommitService: CommitService
    private lateinit var mockDiffService: DiffService
    private lateinit var mockLogger: Logger
    private lateinit var project: Project
    private lateinit var event: AnActionEvent
    private lateinit var mockWorkFlow: WorkFlow
    private lateinit var commitMessageEntryPoint: CommitMessageEntryPoint
    private lateinit var action: GenerateCommitMessageAction

    override fun setUp() {
        super.setUp()
        mockCommitService = Mockito.mock(CommitService::class.java)
        mockDiffService = Mockito.mock(DiffService::class.java)
        mockLogger = Mockito.mock(Logger::class.java)
        project = Mockito.mock(Project::class.java)
        event = Mockito.mock(AnActionEvent::class.java)
        mockWorkFlow = Mockito.mock(WorkFlow::class.java)

        Mockito.`when`(event.project).thenReturn(project)

        commitMessageEntryPoint = CommitMessageEntryPoint(mockCommitService, mockDiffService, mockLogger)
        action = GenerateCommitMessageAction(commitMessageEntryPoint)
    }

    override fun tearDown() {
        try {
            // Ensure that Mockito mocks are reset after each test
            Mockito.reset(mockCommitService, mockDiffService, mockLogger, project, event, mockWorkFlow)
        } finally {
            super.tearDown()
        }
    }

    @DisplayName("커밋 메시지 생성")
    fun testGenerateCommitMessage() {
        // given: Mock 설정
        val diffContent = "Mock diff content"
        val includedChanges = listOf<Change>()

        Mockito.`when`(mockWorkFlow.getIncludedChanges()).thenReturn(includedChanges)
        Mockito.`when`(mockCommitService.getWorkFlow(event)).thenReturn(mockWorkFlow)
        Mockito.`when`(mockDiffService.getDiffInVcs(mockWorkFlow)).thenReturn(diffContent)

        // when: actionPerformed 메서드 호출
        action.actionPerformed(event)

        // then: logger에 올바른 메시지가 기록되었는지 확인
        Mockito.verify(mockLogger).info("생성된 커밋 메시지: \n$diffContent")
    }

    @DisplayName("선택된 변경 사항 없음 오류")
    fun testNoChangesError() {
        // given: Mock 설정, DiffService가 IllegalStateException을 던지도록 설정
        Mockito.`when`(mockWorkFlow.getIncludedChanges()).thenReturn(emptyList())
        Mockito.`when`(mockCommitService.getWorkFlow(event)).thenReturn(mockWorkFlow)
        Mockito.`when`(mockDiffService.getDiffInVcs(mockWorkFlow)).thenThrow(IllegalStateException("선택된 변경 사항이 없습니다."))

        // when: actionPerformed 메서드 호출 및 예외 처리 확인
        try {
            action.actionPerformed(event)
            fail("IllegalStateException이 발생해야 합니다")
        } catch (e: IllegalStateException) {
            assertEquals("선택된 변경 사항이 없습니다.", e.message)
        }
    }

    @DisplayName("CommitWorkflowHandler null 오류")
    fun testNullWorkFlowError() {
        // given: Mock 설정, CommitService가 null을 반환하도록 설정
        Mockito.`when`(mockCommitService.getWorkFlow(event)).thenReturn(null)

        // when: actionPerformed 메서드 호출 및 예외 처리 확인
        try {
            action.actionPerformed(event)
            fail("IllegalStateException이 발생해야 합니다")
        } catch (e: IllegalStateException) {
            assertEquals("CommitWorkflowHandler가 null입니다", e.message)
        }
    }
}
