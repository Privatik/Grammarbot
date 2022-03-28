package com.io.util

import com.io.model.Language
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup

fun inlineKeyBoardMarkup(
    currentLanguage: Language,
    isTranslateButton: Boolean = false,
    isSectionButtons: Boolean = false
): InlineKeyboardMarkup? {
    val markupButtons = mutableListOf<List<InlineKeyboardButton>>()
    if (isTranslateButton){
        markupButtons.add(listOf(keyBoardMarkupTranslate(currentLanguage)))
    }
    if (isSectionButtons){
        markupButtons.add(keyBoardMarkupSections(currentLanguage))
    }
    if (markupButtons.isEmpty()) return null
    return InlineKeyboardMarkup(markupButtons)
}

private fun keyBoardMarkupSections(currentLanguage: Language): List<InlineKeyboardButton> {
    val button1 = InlineKeyboardButton(
        text = "1",
        callback_data = "1"
    )
    val button2 = InlineKeyboardButton(
        text = "2",
        callback_data = "2"
    )
    return listOf(button1, button2)
}

private fun keyBoardMarkupTranslate(currentLanguage: Language): InlineKeyboardButton {
    val button = InlineKeyboardButton(
        text = translateKeyboardMarkup.text(currentLanguage),
        callback_data = translateKeyboardMarkup.callbackData
    )
    return button
}