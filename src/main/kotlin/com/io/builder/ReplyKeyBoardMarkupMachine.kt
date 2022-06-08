package com.io.builder

import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.resourse.Message
import com.io.resourse.createKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.KeyboardButton
import com.io.telegram.ReplyKeyboardMarkup
import com.io.util.extends.inlineKeyBoardMarkup

class ReplyKeyBoardMarkupBuilder(
    private val currentLanguage: Language = Language.RU,
) {
    private val keyboardMarkups = mutableListOf<List<KeyboardButton>>()

    fun addButtons(vararg messages: Message, group: Int = 2): ReplyKeyBoardMarkupBuilder{
        var index = 0
        while (index < messages.size){
            val list = mutableListOf<KeyboardButton>()
            for (i in (index until index + group)){
                val button = messages.getOrNull(i)?.let {
                    val keyBoardMarkup = createKeyboardMarkup(it)
                    KeyboardButton(
                        text = keyBoardMarkup.text(currentLanguage)
                    )
                }
                button?.let { list.add(it) }
            }
            keyboardMarkups.add(list)
            index += group
        }
        return this
    }

    fun build(): ReplyKeyboardMarkup {
        return ReplyKeyboardMarkup(
            keyboard = keyboardMarkups,
            resize_keyboard = true
        )
    }

}