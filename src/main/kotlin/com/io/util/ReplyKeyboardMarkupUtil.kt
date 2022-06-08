package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.builder.ReplyKeyBoardMarkupBuilder
import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.model.LessonState
import com.io.resourse.Message
import com.io.resourse.Resourse
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.ReplyKeyboardMarkup

fun getTaskReplyKeyboardMarkup(language: Language): ReplyKeyboardMarkup {
    return ReplyKeyBoardMarkupBuilder(language)
        .addButtons(Message.FinishLessonMessage)
        .build()
}