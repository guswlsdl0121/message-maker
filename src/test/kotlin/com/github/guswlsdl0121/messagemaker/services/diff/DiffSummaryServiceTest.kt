package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.services.diff.utils.AbstractDiffTest

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
        val change = prepareChange("fileMovedTest")
        val summary = diffSummaryService.summaryDiff(listOf(change))
        verifyResult("fileMovedTest", summary)
    }

    fun testFileAddition() {
        val change = prepareChange("fileAdditionTest")
        val summary = diffSummaryService.summaryDiff(listOf(change))
        verifyResult("fileAdditionTest", summary)
    }

    fun testFileDeletion() {
        val change = prepareChange("fileDeletionTest")
        val summary = diffSummaryService.summaryDiff(listOf(change))
        verifyResult("fileDeletionTest", summary)
    }

    fun testFileModification() {
        val additionalContent = "with new line"
        val change = prepareChange("fileModificationTest", additionalContent)
        val summary = diffSummaryService.summaryDiff(listOf(change))
        verifyResult("fileModificationTest", summary)
    }
}
