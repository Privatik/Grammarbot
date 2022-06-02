package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.resourse.Message
import com.io.resourse.Resourse
import com.io.telegram.InlineKeyboardMarkup

fun getSectionMenuInlineKeyboardMarkup(language: Language, sections: List<SectionEntity>): InlineKeyboardMarkup {
    return InlineKeyBoardMarkupBuilder(language)
        .addButton(Message.TranslateMessage)
        .addSectionButton(sections)
        .build()
}

fun getSectionInlineKeyboardMarkup(language: Language, sectionId: String): InlineKeyboardMarkup {
    return InlineKeyBoardMarkupBuilder(language)
        .addButton(Message.TranslateMessage)
        .addButton(Message.BackLessonMessage)
        .addButton(Message.StartLessonMessage.copy(callBack = Resourse.startLesson + sectionId))
        .build()
}