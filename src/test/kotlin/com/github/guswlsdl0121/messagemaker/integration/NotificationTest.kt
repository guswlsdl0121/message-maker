package com.github.guswlsdl0121.messagemaker.integration//package com.github.guswlsdl0121.messagemaker.integration

import com.github.guswlsdl0121.messagemaker.actions.GenerateCommitMessageAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NotificationTest : BasePlatformTestCase() {
    private lateinit var action: GenerateCommitMessageAction
    private val notifiedMessages = mutableListOf<Triple<String, String, NotificationType>>()

    override fun setUp() {
        super.setUp()
        action = GenerateCommitMessageAction()

        // 알림을 캡처하기 위한 리스너 설정
        project.messageBus.connect().subscribe(Notifications.TOPIC, object : Notifications {
            override fun notify(notification: Notification) {
                println("Notification received: ${notification.title}, ${notification.content}")
                notifiedMessages.add(Triple(notification.title, notification.content, notification.type))
            }
        })
    }

    fun testNoChangesDetected() {
        val context = SimpleDataContext.getProjectContext(project)
        val event = AnActionEvent.createFromAnAction(
            action,
            null,
            ActionPlaces.VCS_HISTORY_TOOLBAR_PLACE,
            context
        )

        WriteCommandAction.runWriteCommandAction(project) {
            println("Action performed")
            action.actionPerformed(event)
        }
        println("After action performed")

        // 비동기 처리를 위한 대기
        PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()
        Thread.sleep(2000) // 2초 대기

        // 알림 확인
        println("Captured notifications: ${notifiedMessages.size}")
        assertTrue("알림이 표시되지 않았습니다", notifiedMessages.isNotEmpty())

        notifiedMessages.forEach { (title, content, type) ->
            println("Received notification: $title, $content, $type")
        }

        // 마지막으로 받은 알림이 예상한 알림인지 확인
        val (title, content, type) = notifiedMessages.last()
        assertEquals("MessageMaker: No Changes Detected", title)
        assertEquals("There are no changes to generate a commit message for.", content)
        assertEquals(NotificationType.WARNING, type)
    }

    override fun tearDown() {
        notifiedMessages.clear()
        super.tearDown()
    }
}
