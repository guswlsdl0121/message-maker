package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.mockito.Mockito.mock
import com.intellij.notification.Notification as IdeaNotification

class NotificationServiceTest : BasePlatformTestCase() {
    private lateinit var project: Project
    private lateinit var notificationService: NotificationService
    private lateinit var notificationGroupManager: NotificationGroupManager

    override fun setUp() {
        super.setUp()
        project = myFixture.project
        notificationGroupManager = mock(NotificationGroupManager::class.java)
        notificationService = NotificationService(project)
    }

    fun testShowNotification() {
        doTest(Notification.COMMIT_MESSAGE_GENERATED, "Test arg")
    }

    fun testShowErrorNotification() {
        doTest(Notification.PROJECT_NOT_FOUND, "Test error")
    }

    private fun doTest(notification: Notification, vararg args: Any?) {
        var notificationReceived = false
        var receivedNotification: IdeaNotification? = null

        val connection = project.messageBus.connect()
        connection.subscribe(Notifications.TOPIC, object : Notifications {
            override fun notify(notification: IdeaNotification) {
                notificationReceived = true
                receivedNotification = notification
            }
        })

        notificationService.show(notification, *args)

        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()

        assertTrue("알림이 수신되지 않았습니다.", notificationReceived)
        assertNotNull("수신된 알림이 null입니다.", receivedNotification)

        receivedNotification?.let { received ->
            assertEquals("알림 제목이 일치하지 않습니다.", "${NotificationConstants.PLUGIN_NAME}: ${notification.title}", received.title)
            assertEquals("알림 내용이 일치하지 않습니다.", notification.content.format(*args), received.content)
            assertEquals("알림 타입이 일치하지 않습니다.", notification.type, received.type)
        }

        connection.disconnect()
    }
}
