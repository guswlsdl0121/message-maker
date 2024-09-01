package com.github.guswlsdl0121.messagemaker.unit.services.diff.generator

import com.github.guswlsdl0121.messagemaker.services.diff.generator.SimpleDiffSummaryGenerator
import com.github.guswlsdl0121.messagemaker.unit.services.diff.AbstractDiffTest
import com.intellij.openapi.vcs.changes.Change

class SimpleDiffSummaryGeneratorTest : AbstractDiffTest() {

    private lateinit var summaryGenerator: SimpleDiffSummaryGenerator

    override fun getRelativeTestDataPath(): String {
        return "summary"
    }

    override fun setUp() {
        super.setUp()
        summaryGenerator = SimpleDiffSummaryGenerator()
    }

    fun testEmptyChanges() {
        val changes = emptyList<Change>()
        val summary = summaryGenerator.generate(changes)
        verifyResult("emptyTest", summary)
    }

    fun testMultipleChanges() {
        val addedChange = prepareChange("multipleTest/added")
        val deletedChange = prepareChange("multipleTest/deleted")
        val modifiedChange = prepareChange("multipleTest/modified", "Modified content")
        val movedChange = prepareChange("multipleTest/moved")

        val changes = listOf(addedChange, deletedChange, modifiedChange, movedChange)
        val summary = summaryGenerator.generate(changes)
        verifyResult("multipleTest", summary)
    }
}
