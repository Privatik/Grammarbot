package com.io.cache.entity

import com.io.model.LessonState
import com.io.model.MessageGroup

data class MessageEntity(
    val id: Int,
    val chatId: String,
    val group: MessageGroup,
)

data class MessageToSection(
    val chatId: String,
    val messageId: Int,
    val sectionId: String
)

data class MessageToTask(
    val chatId: String,
    val messageId: Int,
    val taskId: Long,
    val lessonState: LessonState
)
