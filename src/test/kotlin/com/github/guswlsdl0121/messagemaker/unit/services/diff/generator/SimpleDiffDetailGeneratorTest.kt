package com.github.guswlsdl0121.messagemaker.unit.services.diff.generator

import com.github.guswlsdl0121.messagemaker.services.diff.generator.SimpleDiffDetailGenerator
import com.github.guswlsdl0121.messagemaker.unit.services.diff.AbstractDiffTest
import com.intellij.openapi.vcs.changes.Change

class SimpleDiffDetailGeneratorTest : AbstractDiffTest() {

    private lateinit var diffGenerator: SimpleDiffDetailGenerator

    override fun getRelativeTestDataPath(): String {
        return "diff"
    }

    override fun setUp() {
        super.setUp()
        diffGenerator = SimpleDiffDetailGenerator()
    }

    fun testEmptyChanges() {
        val changes = emptyList<Change>()
        val summary = diffGenerator.generate(changes)
        verifyResult("emptyTest", summary)
    }

    fun testMultipleChanges() {
        val addedChange = prepareChange("multipleTest/added")
        val deletedChange = prepareChange("multipleTest/deleted")
        val modifiedChange = prepareChange("multipleTest/modified", "Modified content")
        val movedChange = prepareChange("multipleTest/moved")

        val changes = listOf(addedChange, deletedChange, modifiedChange, movedChange)
        val summary = diffGenerator.generate(changes)
        verifyResult("multipleTest", summary)
    }
}
