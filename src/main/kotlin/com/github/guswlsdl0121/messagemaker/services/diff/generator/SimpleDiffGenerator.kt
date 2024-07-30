package com.github.guswlsdl0121.messagemaker.services.diff.generator

class SimpleDiffGenerator : DiffGenerator {
    override fun generate(beforeContent: String, afterContent: String): String {
        val beforeLines = beforeContent.lines()
        val afterLines = afterContent.lines()

        val diff = mutableListOf<String>()
        var i = 0
        var j = 0

        while (i < beforeLines.size || j < afterLines.size) {
            when {
                i >= beforeLines.size -> {
                    // 원본 내용이 모두 처리되었고, 새 내용에 추가된 줄들을 처리
                    diff.add("+${afterLines[j]}")
                    j++
                }
                j >= afterLines.size -> {
                    // 새 내용이 모두 처리되었고, 원본 내용에서 삭제된 줄들을 처리
                    diff.add("-${beforeLines[i]}")
                    i++
                }
                beforeLines[i] == afterLines[j] -> {
                    // 양쪽 내용이 동일한 경우, 변경 없이 다음 줄로 넘어감
                    i++
                    j++
                }
                else -> {
                    // 내용이 다른 경우, 삭제와 추가로 처리
                    if (beforeLines[i].isNotEmpty()) {
                        diff.add("-${beforeLines[i]}")
                    }
                    if (afterLines[j].isNotEmpty()) {
                        diff.add("+${afterLines[j]}")
                    }
                    i++
                    j++
                }
            }
        }

        return diff.joinToString("\n")
    }
}
