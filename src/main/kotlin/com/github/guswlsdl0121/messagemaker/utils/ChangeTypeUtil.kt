package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.vcs.changes.Change

object ChangeTypeUtil {
    fun countChangeTypes(changes: List<Change>): MutableMap<Change.Type, Int> {
        val changeTypeCounts = mutableMapOf<Change.Type, Int>()
        changes.forEach { change ->
            val type = change.type
            changeTypeCounts[type] = changeTypeCounts.getOrDefault(type, 0) + 1
        }
        return changeTypeCounts
    }
}
