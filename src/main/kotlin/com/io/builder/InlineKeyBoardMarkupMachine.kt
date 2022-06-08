package com.io.builder

import com.io.cache.entity.SectionEntity
import com.io.model.Language
import com.io.resourse.Message
import com.io.resourse.createKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.KeyboardButton
import com.io.util.extends.inlineKeyBoardMarkup

class InlineKeyBoardMarkupBuilder(
    private val currentLanguage: Language = Language.RU,
) {
    private val keyboardMarkups = mutableListOf<List<InlineKeyboardButton>>()

    fun addButtons(vararg messages: Message, group: Int = 2): InlineKeyBoardMarkupBuilder{
        var index = 0
        while (index < messages.size){
            val list = mutableListOf<InlineKeyboardButton>()
            for (i in (index until index + group)){
                val button = messages.getOrNull(i)?.let {
                    val keyBoardMarkup = createKeyboardMarkup(it)
                    InlineKeyboardButton(
                        text = keyBoardMarkup.text(currentLanguage),
                        callback_data = keyBoardMarkup.callbackData
                    )
                }
                button?.let { list.add(it) }
            }
            keyboardMarkups.add(list)
            index += group
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