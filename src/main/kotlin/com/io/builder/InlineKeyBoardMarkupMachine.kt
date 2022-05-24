package com.io.builder

import com.io.cache.SectionCache
import com.io.model.Language
import com.io.resourse.KeyboardMarkup
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import com.io.util.extends.inlineKeyBoardMarkup
import org.koin.java.KoinJavaComponent.inject

object InlineKeyBoardMarkupMachine {
    private val sectionCache: SectionCache by inject(SectionCache::class.java)

    class Builder constructor(private val currentLanguage: Language = Language.RU){
        private val keyboardMarkups = mutableListOf<List<InlineKeyboardButton>>()

        suspend fun addTranslateButton(): Builder{
            val button = InlineKeyboardButton(
                text = translateKeyboardMarkup.text(currentLanguage),
                callback_data = translateKeyboardMarkup.callbackData
            )
            keyboardMarkups.add(listOf(button))
            return this
        }

        suspend fun addSectionButton(): Builder{
            val sectionEntities = sectionCache.getAllSection()
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

        suspend fun build(): InlineKeyboardMarkup{
            return InlineKeyboardMarkup(keyboardMarkups)
        }
    }
}