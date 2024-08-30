package com.github.guswlsdl0121.messagemaker.services.notification

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.notification.Notification
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class CommitMessageNotificationServiceTest : BasePlatformTestCase() {
    private lateinit var project: Project
    private lateinit var notificationService: NotificationService
    private lateinit var notificationGroup: NotificationGroup
    private val notifiedMessages = mutableListOf<Triple<String, String, NotificationType>>()

    override fun setUp() {
        super.setUp()
        project = myFixture.project
        notificationService = NotificationService(project)
        notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(NotificationConstants.NOTIFICATION_GROUP_ID)

        // 알림을 가로채기 위한 리스너 설정
        val messageBus = project.messageBus
        messageBus.connect().subscribe(Notifications.TOPIC, object : Notifications {
            override fun notify(notification: Notification) {
                notifiedMessages.add(Triple(notification.title, notification.content, notification.type))
            }
        })
    }

    fun testShowNotification() {
        doTest(CommitMessageNotification.COMMIT_MESSAGE_GENERATED, "Test arg")
    }

    fun testShowErrorNotification() {
        doTest(CommitMessageNotification.PROJECT_NOT_FOUND, "Test error")
    }

    private fun doTest(notification: CommitMessageNotification, vararg args: Any?) {
        notifiedMessages.clear()

        notificationService.show(notification, *args)

        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()

        assertTrue("No notification was shown", notifiedMessages.isNotEmpty())
        val (title, content, type) = notifiedMessages.last()

        assertEquals("${NotificationConstants.PLUGIN_NAME}: ${notification.title}", title)
        assertEquals(notification.content.format(*args), content)
        assertEquals(notification.type, type)
    }

    override fun tearDown() {
        notifiedMessages.clear()
        super.tearDown()
    }
}
