package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.builder.ReplyKeyBoardMarkupBuilder
import com.io.cache.entity.Entity
import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.model.LessonState
import com.io.resourse.Message
import com.io.resourse.Resourse
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.ReplyKeyboardMarkup
import com.io.util.extends.createSimpleMessage

fun getTaskReplyKeyboardMarkup(task: Entity.Task, language: Language): ReplyKeyboardMarkup {
    return ReplyKeyBoardMarkupBuilder(language)
        .apply {
            if (task.state == LessonState.PUT){
                val variant = (task as Entity.Task.PutTaskEntity).variants.map { it.createSimpleMessage() }
                addButtons(variant, 3)
            }
        }
        .addButtons(Message.FinishLessonMessage)
        .build()
}