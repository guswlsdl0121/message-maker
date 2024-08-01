package com.github.guswlsdl0121.messagemaker.utils

import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vcs.changes.Change

object ChangeTypeUtil {
    fun countChangeTypes(changes: List<Change>): MutableMap<Change.Type, Int> {
        val changeTypeCounts = mutableMapOf<Change.Type, Int>()
        changes.forEach { change ->
            val type = refineChangeType(change)
            changeTypeCounts[type] = changeTypeCounts.getOrDefault(type, 0) + 1
        }
        return changeTypeCounts
    }

    fun refineChangeType(change: Change): Change.Type {
        val beforeRevision = change.beforeRevision
        val afterRevision = change.afterRevision

        if (beforeRevision == null) return Change.Type.NEW
        if (afterRevision == null) return Change.Type.DELETED

        val beforeFile = beforeRevision.file
        val afterFile = afterRevision.file

        // 경로가 다르면 MOVED로 처리
        if (!Comparing.equal(beforeFile, afterFile)) {
            // 경로가 다르고 내용도 다르면 MODIFICATION으로 처리
            return if (beforeRevision.content != afterRevision.content) {
                Change.Type.MODIFICATION
            } else {
                Change.Type.MOVED
            }
        }

        // 경로가 같고 내용이 다르면 MODIFICATION으로 처리
        if (beforeRevision.content != afterRevision.content) {
            return Change.Type.MODIFICATION
        }

        // 경로와 내용이 동일하면 MODIFICATION으로 처리
        return Change.Type.MODIFICATION
    }
}
