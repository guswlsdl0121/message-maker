package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.testFramework.LoggedErrorProcessor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.replaceService
import com.intellij.util.ThrowableRunnable

class ActionExceptionHandlerTest : BasePlatformTestCase() {

    private lateinit var actionExceptionHandler: ActionExceptionHandler
    private lateinit var notificationService: NotificationService

    override fun setUp() {
        super.setUp()
        notificationService = NotificationService(myFixture.project)
        myFixture.project.replaceService(NotificationService::class.java, notificationService, testRootDisposable)
        actionExceptionHandler = ActionExceptionHandler(myFixture.project)
    }

    fun testNoChangesException() {
        testExceptionHandling(
            NoChangesException(),
            "MessageMaker: No Changes Detected",
            NotificationType.WARNING
        )
    }

    fun testProjectNullException() {
        testExceptionHandling(
            ProjectNullException(),
            "MessageMaker: Project Not Found",
            NotificationType.ERROR
        )
    }

    fun testHandleUnexpectedException() {
        testUnexpectedExceptionHandling(
            RuntimeException("Test unexpected exception"),
            "MessageMaker: Unexpected Error",
            NotificationType.ERROR,
            "An unexpected error occurred. Please check the logs for more details."
        )
    }

    // 일반적인 예외 처리를 테스트하는 메서드
    private fun testExceptionHandling(exception: Exception, expectedTitle: String, expectedType: NotificationType) {
        val notificationShown = setupListenerAndHandleException(expectedTitle, expectedType) {
            //실제 동작 수행
            actionExceptionHandler.handle(exception)
        }

        assertTrue(notificationShown)
    }

    // 예상치 못한 예외 처리를 테스트하는 메서드
    private fun testUnexpectedExceptionHandling(
        exception: Exception,
        expectedTitle: String,
        expectedType: NotificationType,
        expectedContent: String
    ) {
        val loggedErrors = mutableListOf<String>()

        // LoggedErrorProcessor를 익명 객체로 오버라이드하여 커스텀 로깅 동작 정의
        val customErrorProcessor = object : LoggedErrorProcessor() {
            override fun processError(
                category: String,
                message: String?,
                t: Throwable?,
                details: Array<out String>
            ): Boolean {
                // 에러 메시지를 loggedErrors 리스트에 추가하고 false를 반환하여 기본 로그 처리를 막음
                message?.let { loggedErrors.add(it) }
                return false
            }
        }

        val notificationShown = setupListenerAndHandleException(expectedTitle, expectedType, expectedContent) {
            // LoggedErrorProcessor.executeWith를 사용하여 커스텀 에러 프로세서로 감싼체로 실제 Error로깅 테스트
            LoggedErrorProcessor.executeWith(customErrorProcessor, ThrowableRunnable<RuntimeException> {
                actionExceptionHandler.handle(exception)
            })
        }

        assertTrue(loggedErrors.any { it.contains("예상치 못한 오류 발생") })
        assertTrue(notificationShown)
    }

    // 알림 리스너를 설정하고 주어진 작업을 실행하는 헬퍼 메서드
    private fun setupListenerAndHandleException(
        expectedTitle: String,
        expectedType: NotificationType,
        expectedContent: String? = null,
        action: () -> Unit
    ): Boolean {
        //알람을 추적하는 변수
        var notificationShown = false
        val listener = object : Notifications {
            // notify 메서드를 오버라이드하여 알림 검증 로직 구현
            override fun notify(notification: Notification) {
                // 알림의 제목, 타입, 내용이 예상과 일치하는지 검증
                assertEquals(expectedTitle, notification.title)
                assertEquals(expectedType, notification.type)
                expectedContent?.let { assertEquals(it, notification.content) }
                // 알림이 표시되었음을 표시
                notificationShown = true
            }
        }

        // 프로젝트의 메시지 버스에 리스너를 등록함으로써 애플리케이션 내의 다양한 컴포넌트 간 통신을 가능하게 함
        myFixture.project.messageBus.connect(testRootDisposable).subscribe(Notifications.TOPIC, listener)

        // 주어진 action 실행 (예외 처리 로직)
        action()

        // 알림이 표시되었는지 여부 반환
        return notificationShown
    }
}
