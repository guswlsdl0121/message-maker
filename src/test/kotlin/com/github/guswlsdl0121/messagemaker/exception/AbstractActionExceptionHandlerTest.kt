package com.github.guswlsdl0121.messagemaker.exception

import com.github.guswlsdl0121.messagemaker.services.notification.NotificationService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.testFramework.LoggedErrorProcessor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.replaceService
import com.intellij.util.ThrowableRunnable

abstract class AbstractActionExceptionHandlerTest : BasePlatformTestCase() {
    private lateinit var actionExceptionHandler: ActionExceptionHandler
    private lateinit var notificationService: NotificationService

    override fun setUp() {
        super.setUp()
        notificationService = NotificationService(myFixture.project)
        myFixture.project.replaceService(NotificationService::class.java, notificationService, testRootDisposable)
        actionExceptionHandler = ActionExceptionHandler(myFixture.project)
    }

    protected fun testExceptionHandling(exception: Exception, expectedTitle: String, expectedType: NotificationType) {
        val notificationShown = setupListenerAndHandleException(expectedTitle, expectedType) {
            actionExceptionHandler.handle(exception)
        }
        assertTrue(notificationShown)
    }

    protected fun testUnexpectedExceptionHandling(
        exception: Exception,
        expectedTitle: String,
        expectedType: NotificationType,
        expectedContent: String
    ) {
        val loggedErrors = mutableListOf<String>()
        val customErrorProcessor = createCustomErrorProcessor(loggedErrors)

        val notificationShown = setupListenerAndHandleException(expectedTitle, expectedType, expectedContent) {
            LoggedErrorProcessor.executeWith(customErrorProcessor, ThrowableRunnable<RuntimeException> {
                actionExceptionHandler.handle(exception)
            })
        }

        assertTrue(loggedErrors.any { it.contains("예상치 못한 오류 발생") })
        assertTrue(notificationShown)
    }

    private fun setupListenerAndHandleException(
        expectedTitle: String,
        expectedType: NotificationType,
        expectedContent: String? = null,
        action: () -> Unit
    ): Boolean {
        var notificationShown = false
        val listener = createNotificationListener(expectedTitle, expectedType, expectedContent) {
            notificationShown = true
        }

        myFixture.project.messageBus.connect(testRootDisposable).subscribe(Notifications.TOPIC, listener)
        action()
        return notificationShown
    }

    private fun createCustomErrorProcessor(loggedErrors: MutableList<String>): LoggedErrorProcessor {
        return object : LoggedErrorProcessor() {
            override fun processError(
                category: String,
                message: String?,
                t: Throwable?,
                details: Array<out String>
            ): Boolean {
                message?.let { loggedErrors.add(it) }
                return false
            }
        }
    }

    private fun createNotificationListener(
        expectedTitle: String,
        expectedType: NotificationType,
        expectedContent: String?,
        onNotify: () -> Unit
    ): Notifications {
        return object : Notifications {
            override fun notify(notification: Notification) {
                assertEquals(expectedTitle, notification.title)
                assertEquals(expectedType, notification.type)
                expectedContent?.let { assertEquals(it, notification.content) }
                onNotify()
            }
        }
    }
}
