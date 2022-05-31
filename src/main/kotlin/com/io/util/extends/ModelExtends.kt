package com.io.util.extends

import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.resourse.KeyboardMarkup
import com.io.resourse.Message
import com.io.resourse.Resourse
import com.io.resourse.createKeyboardMarkup
import com.io.telegram.InlineKeyboardButton

fun UserEntity.anotherLanguage(): Language{
    return if (currentLanguage == Language.EN){
        Language.RU
    } else {
        Language.EN
    }
}

fun SectionEntity.createMessage():Message{
    return object : Message {
        override val ru: String
            get() = titleRu
        override val en: String
            get() = titleEn

    }
}

fun SectionRuleEntity.createMessage():Message{
    return object : Message {
        override val ru: String
            get() = ruleRu
        override val en: String
            get() = ruleEn

    }
}

fun SectionEntity.inlineKeyBoardMarkup(currentLanguage: Language): InlineKeyboardButton {
    val message = createMessage()
    val button = createKeyboardMarkup(message, "${Resourse.section}$id")
    return button.inlineKeyBoardMarkup(currentLanguage)
}

fun KeyboardMarkup.inlineKeyBoardMarkup(currentLanguage: Language): InlineKeyboardButton {
    return InlineKeyboardButton(
        text = text(currentLanguage),
        callback_data = callbackData
    )
}