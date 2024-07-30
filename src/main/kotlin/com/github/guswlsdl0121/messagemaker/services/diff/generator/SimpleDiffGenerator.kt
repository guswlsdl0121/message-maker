package com.github.guswlsdl0121.messagemaker.services.diff.generator

class SimpleDiffGenerator : DiffGenerator {
    override fun generate(beforeContent: String, afterContent: String): String {
        val beforeLines = beforeContent.lines()
        val afterLines = afterContent.lines()

        val commonPrefixLength = findCommonPrefixLength(beforeLines, afterLines)
        val commonSuffixLength = findCommonSuffixLength(beforeLines, afterLines, commonPrefixLength)

        val beforeDiff = extractDiff(beforeLines, commonPrefixLength, commonSuffixLength, "-")
        val afterDiff = extractDiff(afterLines, commonPrefixLength, commonSuffixLength, "+")

        return (beforeDiff + afterDiff).joinToString("\n")
    }

    private fun findCommonPrefixLength(beforeLines: List<String>, afterLines: List<String>): Int {
        var commonPrefix = 0
        while (commonPrefix < beforeLines.size && commonPrefix < afterLines.size && beforeLines[commonPrefix] == afterLines[commonPrefix]) {
            commonPrefix++
        }
        return commonPrefix
    }

    private fun findCommonSuffixLength(beforeLines: List<String>, afterLines: List<String>, commonPrefixLength: Int): Int {
        var commonSuffix = 0
        while (commonSuffix + commonPrefixLength < beforeLines.size &&
            commonSuffix + commonPrefixLength < afterLines.size &&
            beforeLines[beforeLines.size - 1 - commonSuffix] == afterLines[afterLines.size - 1 - commonSuffix]) {
            commonSuffix++
        }
        return commonSuffix
    }

    private fun extractDiff(lines: List<String>, commonPrefixLength: Int, commonSuffixLength: Int, prefix: String): List<String> {
        return lines.subList(commonPrefixLength, lines.size - commonSuffixLength)
            .map { "$prefix$it" }
    }
}
