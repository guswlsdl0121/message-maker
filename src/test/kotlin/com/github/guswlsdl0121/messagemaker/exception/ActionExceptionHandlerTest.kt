package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.replaceService

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
        val exception = NoChangesException()

        val listener = object : Notifications {
            override fun notify(notification: Notification) {
                assertEquals("MessageMaker: No Changes Detected", notification.title)
                assertEquals(NotificationType.WARNING, notification.type)
            }
        }
        myFixture.project.messageBus.connect(testRootDisposable).subscribe(Notifications.TOPIC, listener)

        actionExceptionHandler.handle(exception)
    }

    fun testProjectNullException() {
        val exception = ProjectNullException()

        val listener = object : Notifications {
            override fun notify(notification: Notification) {
                assertEquals("MessageMaker: Project Not Found", notification.title)
                assertEquals(NotificationType.ERROR, notification.type)
            }
        }
        myFixture.project.messageBus.connect(testRootDisposable).subscribe(Notifications.TOPIC, listener)

        actionExceptionHandler.handle(exception)
    }


    fun testHandleUnexpectedException() {
        TODO("ERROR 수준의 로그가 발생하면 IntelliJ에서 바로 Test를 터뜨린다. 어떻게 해야될까?")
    }
}
