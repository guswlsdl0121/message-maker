package com.github.guswlsdl0121.messagemaker.services.diff

import com.github.guswlsdl0121.messagemaker.prompt.DiffPrompter
import com.github.guswlsdl0121.messagemaker.handler.WorkFlow

class DefaultDiffService(private val diffPrompter: DiffPrompter) : DiffService {
    override fun getDiffInVcs(workFlow: WorkFlow): String {
        val includedChanges = workFlow.getIncludedChanges()
            .takeIf { it.isNotEmpty() } ?: error("선택된 변경 사항이 없습니다.")

        return diffPrompter.generate(includedChanges)
    }
}

