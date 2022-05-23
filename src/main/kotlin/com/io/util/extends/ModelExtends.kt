package com.io.util.extends

import com.io.cache.entity.SectionRuleEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.resourse.KeyboardMarkup
import com.io.resourse.createKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.KeyboardButton

fun UserEntity.anotherLanguage(): Language{
    return if (currentLanguage == Language.EN){
        Language.RU
    } else {
        Language.EN
    }
}

fun SectionRuleEntity.inlineKeyBoardMarkup(currentLanguage: Language): InlineKeyboardButton {
    val button = createKeyboardMarkup()
    return InlineKeyboardButton(
        text = markup.text(currentLanguage),
        callback_data = markup.callbackData
    )
}

fun KeyboardMarkup.inlineKeyBoardMarkup(currentLanguage: Language): InlineKeyboardButton {
    return InlineKeyboardButton(
        text = text(currentLanguage),
        callback_data = callbackData
    )
}