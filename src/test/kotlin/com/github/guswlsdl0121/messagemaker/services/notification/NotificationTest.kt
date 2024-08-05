package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NotificationServiceTest : BasePlatformTestCase() {
    private lateinit var project: Project
    private lateinit var notificationService: NotificationService
    private lateinit var notificationGroupManager: NotificationGroupManager

    override fun setUp() {
        super.setUp()
        project = myFixture.project
        notificationGroupManager = mockk(relaxed = true)
        notificationService = NotificationService(project)
    }

    fun testShowNotification() {
        doTest(Notification.COMMIT_MESSAGE_GENERATED, "Test arg")
    }

    fun testShowErrorNotification() {
        doTest(Notification.PROJECT_NOT_FOUND, "Test error")
    }

    private fun doTest(notification: Notification, vararg args: Any?) {
        val mockNotifications = mockk<Notifications>()
        every { mockNotifications.notify(any()) } returns Unit

        val connection = project.messageBus.connect()
        connection.subscribe(Notifications.TOPIC, mockNotifications)

        notificationService.show(notification, *args)

        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()

        verify {
            mockNotifications.notify(match { ideaNotification ->
                ideaNotification.title == "${NotificationConstants.PLUGIN_NAME}: ${notification.title}" &&
                        ideaNotification.content == notification.content.format(*args) &&
                        ideaNotification.type == notification.type
            })
        }

        connection.disconnect()
    }
}
