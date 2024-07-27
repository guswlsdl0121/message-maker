package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.handler.WorkFlow

interface DiffService {
    fun getDiffInVcs(workFlow: WorkFlow): String
}
