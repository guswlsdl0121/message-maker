package com.github.guswlsdl0121.messagemaker.services.diff.generator

interface DiffGenerator {
    fun generate(beforeContent: String, afterContent: String): String
}
