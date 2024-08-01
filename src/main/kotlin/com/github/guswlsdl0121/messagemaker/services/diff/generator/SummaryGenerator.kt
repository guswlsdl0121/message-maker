package com.github.guswlsdl0121.messagemaker.services.diff.generator

import com.intellij.openapi.vcs.changes.Change

interface SummaryGenerator {
    fun generate(changes: List<Change>): String
}
