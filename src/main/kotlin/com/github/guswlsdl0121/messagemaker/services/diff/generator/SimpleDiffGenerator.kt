package com.github.guswlsdl0121.messagemaker.services.diff.generator

class SimpleDiffGenerator : DiffGenerator {
    override fun generate(beforeContent: String, afterContent: String): String {
        val beforeLines = beforeContent.lines()
        val afterLines = afterContent.lines()

        return buildString {
            var i = 0
            var j = 0

            while (i < beforeLines.size || j < afterLines.size) {
                when {
                    i >= beforeLines.size -> append("+${afterLines[j++]}\n")
                    j >= afterLines.size -> append("-${beforeLines[i++]}\n")
                    beforeLines[i] == afterLines[j] -> {
                        i++
                        j++
                    }

                    else -> {
                        if (beforeLines[i].isNotEmpty()) append("-${beforeLines[i]}\n")
                        if (afterLines[j].isNotEmpty()) append("+${afterLines[j]}\n")
                        i++
                        j++
                    }
                }
            }
        }.trimEnd()
    }
}
