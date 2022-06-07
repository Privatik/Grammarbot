package com.io.builder

import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.resourse.Message
import com.io.resourse.createKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import com.io.util.extends.inlineKeyBoardMarkup

class InlineKeyBoardMarkupBuilder(
    private val currentLanguage: Language = Language.RU,
) {
    private val keyboardMarkups = mutableListOf<List<InlineKeyboardButton>>()

    fun addButton(message: Message): InlineKeyBoardMarkupBuilder{
        val keyBoardMarkup = createKeyboardMarkup(message)
        val button = InlineKeyboardButton(
            text = keyBoardMarkup.text(currentLanguage),
            callback_data = keyBoardMarkup.callbackData
        )
        keyboardMarkups.add(listOf(button))
        return this
    }

    fun addButtons(vararg messages: Message): InlineKeyBoardMarkupBuilder{
        for (i in messages.indices step 2){

            val button1 = createKeyboardMarkup(messages[i]).let {
                InlineKeyboardButton(
                    text = it.text(currentLanguage),
                    callback_data = it.callbackData
                )
            }

            val button2 = messages.getOrNull(i + 1)?.let {
                val keyBoardMarkup2 = createKeyboardMarkup(it)
                InlineKeyboardButton(
                    text = keyBoardMarkup2.text(currentLanguage),
                    callback_data = keyBoardMarkup2.callbackData
                )
            }

            keyboardMarkups.add(
                if (button2 != null){
                    listOf(button1, button2)
                } else {
                    listOf(button1)
                }
            )
        }
        return this
    }

    fun addSectionButton(sectionEntities: List<SectionEntity>): InlineKeyBoardMarkupBuilder{
        for (i in sectionEntities.indices step 2) {
            val firstItem = sectionEntities[i].inlineKeyBoardMarkup(currentLanguage)
            val secondItem = sectionEntities.getOrNull(i + 1)?.inlineKeyBoardMarkup(currentLanguage)

            keyboardMarkups.add(
                if (secondItem != null){
                    listOf(firstItem, secondItem)
                } else {
                    listOf(firstItem)
                }
            )
        }
        return this
    }

    fun build(): InlineKeyboardMarkup{
        return InlineKeyboardMarkup(keyboardMarkups)
    }

}