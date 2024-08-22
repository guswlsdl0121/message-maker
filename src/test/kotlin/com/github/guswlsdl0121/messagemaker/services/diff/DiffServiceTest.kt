package com.github.guswlsdl0121.messagemaker.services.diff

import com.intellij.openapi.vcs.changes.Change

class DiffServiceTest : AbstractDiffTest() {

    private lateinit var diffService: DiffService

    override fun getRelativeTestDataPath(): String {
        return "diffSummary"
    }

    override fun setUp() {
        super.setUp()
        diffService = DiffService(project)
    }

    fun testEmptyChanges() {
        val changes = emptyList<Change>()
        val summary = diffService.getDiff(changes)
        verifyResult("emptyTest", summary)
    }

    fun testMultipleChanges() {
        val addedChange = prepareChange("multipleTest/added")
        val deletedChange = prepareChange("multipleTest/deleted")
        val modifiedChange = prepareChange("multipleTest/modified", "Modified content")
        val movedChange = prepareChange("multipleTest/moved")

        val changes = listOf(addedChange, deletedChange, modifiedChange, movedChange)
        val summary = diffService.getDiff(changes)
        verifyResult("multipleTest", summary)
    }
}
