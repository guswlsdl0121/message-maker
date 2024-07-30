package com.github.guswlsdl0121.messagemaker.services.diff.generator

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SimpleDiffGeneratorTest : BasePlatformTestCase() {
    private val diffGenerator = SimpleDiffGenerator()

    fun test_빈줄() {
        doTest("", "", "")
    }

    fun test_라인제거() {
        doTest(
            "Old line",
            "",
            "-Old line"
        )
    }

    fun test_라인추가() {
        doTest(
            "",
            "New line",
            "+New line"
        )
    }

    fun test_라인수정() {
        doTest(
            "Old line\nCommon line",
            "New line\nCommon line",
            "-Old line\n+New line"
        )
    }

    fun test_여러변경() {
        doTest(
            "Line 1\nOld line\nLine 3\nCommon line",
            "Line 1\nNew line\nAdded line\nCommon line",
            "-Old line\n+New line\n-Line 3\n+Added line"
        )
    }

    fun test_실제코드() {
        doTest(
            """
        fun example() {
            println("Old line 1")
            println("Common line")
            println("Old line 3")
        }
        """.trimIndent(),
            """
        fun example() {
            println("New line 1")
            println("Common line")
            println("New line 3")
            println("Added line")
        }
        """.trimIndent(),
            """
        -    println("Old line 1")
        +    println("New line 1")
        -    println("Old line 3")
        +    println("New line 3")
        -}
        +    println("Added line")
        +}
        """.trimIndent()
        )
    }

    private fun doTest(before: String, after: String, expected: String) {
        val result = diffGenerator.generate(before, after)
        assertEquals(expected, result)
    }
}
