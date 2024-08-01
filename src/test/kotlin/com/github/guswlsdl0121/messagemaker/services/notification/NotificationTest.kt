package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.notification.Notification as IdeaNotification

class NotificationTest : BasePlatformTestCase() {
    private lateinit var project: Project

    override fun setUp() {
        super.setUp()
        project = myFixture.project
    }

    fun test_커밋메시지_생성알림() {
        doTest(
            notification = Notification.COMMIT_MESSAGE_GENERATED,
            expectedTitle = "Commit Message Generated",
            expectedContent = "Your commit message has been successfully generated.",
            expectedType = NotificationType.INFORMATION
        )
    }

    fun test_오류알림() {
        val errorMessage = "Test error"
        doTest(
            notification = Notification.GENERATION_FAILED,
            expectedTitle = "Generation Failed",
            expectedContent = "Failed to generate commit message: $errorMessage",
            expectedType = NotificationType.ERROR,
            args = arrayOf(errorMessage)
        )
    }

    private fun doTest(
        notification: Notification,
        expectedTitle: String,
        expectedContent: String,
        expectedType: NotificationType,
        vararg args: Any?
    ) {
        var notificationReceived = false
        var receivedNotification: IdeaNotification? = null

        val connection = project.messageBus.connect()
        connection.subscribe(Notifications.TOPIC, object : Notifications {
            override fun notify(notification: IdeaNotification) {
                notificationReceived = true
                receivedNotification = notification
            }
        })

        notification.show(project, *args)

        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()

        assertTrue("알림이 수신되지 않았습니다.", notificationReceived)
        assertNotNull("수신된 알림이 null입니다.", receivedNotification)

        receivedNotification?.let { received ->
            assertEquals("알림 제목이 일치하지 않습니다.", "${NotificationService.getPluginName()}: $expectedTitle", received.title)
            assertEquals("알림 내용이 일치하지 않습니다.", expectedContent, received.content)
            assertEquals("알림 타입이 일치하지 않습니다.", expectedType, received.type)
        }

        connection.disconnect()
    }
}
