package com.github.guswlsdl0121.messagemaker.exception


sealed class CustomException(message: String) : Exception(message)
class NoChangesException : CustomException("변경사항이 없거나 CommitWorkflowHandler가 설정되지 않았습니다.")
