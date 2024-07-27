package com.github.guswlsdl0121.messagemaker.handler

import com.intellij.openapi.vcs.changes.Change

interface WorkFlow {
    fun getIncludedChanges(): List<Change>
}
