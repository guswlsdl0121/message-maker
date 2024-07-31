package com.github.guswlsdl0121.messagemaker.exception


sealed class MessageMakerException(message: String) : Exception(message)
class NoChangesException : MessageMakerException("변경사항이 없거나 CommitWorkflowHandler가 설정되지 않았습니다.")
class ProjectNullException : MessageMakerException("현재 프로젝트가 정상적으로 인식되지 않습니다.")
