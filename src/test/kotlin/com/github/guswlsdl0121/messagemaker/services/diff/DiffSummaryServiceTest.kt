package com.github.guswlsdl0121.messagemaker.services.diff

import com.intellij.openapi.vcs.changes.Change

class DiffSummaryServiceTest : AbstractDiffTest() {

    private lateinit var diffSummaryService: DiffSummaryService

    override fun getRelativeTestDataPath(): String {
        return "diffSummary"
    }

    override fun setUp() {
        super.setUp()
        diffSummaryService = DiffSummaryService(project)
    }

    fun testFileMoved() {
        val changes = emptyList<Change>()
        val summary = diffSummaryService.summaryDiff(changes)
        verifyResult("emptyTest", summary)
    }

    fun testMultipleChanges() {
        val addedChange = prepareChange("multipleTest/added")
        val deletedChange = prepareChange("multipleTest/deleted")
        val modifiedChange = prepareChange("multipleTest/modified", "Modified content")

        val changes = listOf(addedChange, deletedChange, modifiedChange)
        val summary = diffSummaryService.summaryDiff(changes)
        verifyResult("multipleTest", summary)
    }
}
