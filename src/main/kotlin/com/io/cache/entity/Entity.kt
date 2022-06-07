package com.io.cache.entity

import com.io.model.LessonState
import com.io.model.MessageGroup

sealed interface Entity{
    data class MessageEntity(
        val id: Int,
        val chatId: String,
        val group: MessageGroup,
    ): Entity

    data class SectionRuleEntity(
        val id: String,
        val ruleEn: String,
        val ruleRu: String
    ): Entity

    sealed class Task: Entity{
        abstract val id: Long
        abstract val sectionId: String
        abstract val field: String
        abstract val rightAnswer: String
        abstract val state: LessonState

        data class WriteTaskEntity(
            override val id: Long,
            override val field: String,
            override val rightAnswer: String,
            override val sectionId: String
        ): Task() {
            override val state: LessonState = LessonState.WRITE
        }

        data class PutTaskEntity(
            override val id: Long,
            override val field: String,
            val variants: List<String>,
            override val rightAnswer: String,
            override val sectionId: String
        ): Task() {
            override val state: LessonState = LessonState.PUT
        }
    }
}
