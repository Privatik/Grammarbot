package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.model.LessonState
import com.io.resourse.Message
import com.io.resourse.Resourse
import com.io.telegram.InlineKeyboardMarkup

fun getSectionMenuInlineKeyboardMarkup(language: Language, sections: List<SectionEntity>): InlineKeyboardMarkup {
    return InlineKeyBoardMarkupBuilder(language)
        .addButtons(Message.TranslateMessage)
        .addSectionButton(sections)
        .build()
}

fun getSectionInlineKeyboardMarkup(language: Language): InlineKeyboardMarkup {
    return InlineKeyBoardMarkupBuilder(language)
        .addButtons(Message.TranslateMessage)
        .addButtons(Message.BackLessonMessage)
        .addButtons(Message.PutTaskMessage, Message.WriteTaskMessage)
        .build()
}

fun getAnswerInlineKeyboardMarkup(language: Language): InlineKeyboardMarkup {
    return InlineKeyBoardMarkupBuilder(language)
        .addButtons(Message.TranslateMessage)
        .addButtons(Message.NextMessage)
        .build()
}