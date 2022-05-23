package com.io.builder

import com.io.cache.SectionCache
import com.io.model.Language
import com.io.resourse.KeyboardMarkup
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import org.koin.java.KoinJavaComponent.inject

object InlineKeyBoardMarkup {
    val sectionCache: SectionCache by inject(SectionCache::class.java)

    private fun createKeyBoardMarkup(currentLanguage: Language, markup: KeyboardMarkup): InlineKeyboardButton{
        return InlineKeyboardButton(
            text = markup.text(currentLanguage),
            callback_data = markup.callbackData
        )
    }

    class Builder {
        val keyboardMarkups = mutableListOf<List<InlineKeyboardButton>>()
        private var currentLanguage = Language.RU

        suspend fun changeLanguage(language: Language){
            currentLanguage = language
        }

        suspend fun addTranslateButton(){
            val button = InlineKeyboardButton(
                text = translateKeyboardMarkup.text(currentLanguage),
                callback_data = translateKeyboardMarkup.callbackData
            )
            keyboardMarkups.add(listOf(button))
        }

        suspend fun addSectionButton(){
            val sectionEntities = sectionCache.getAllSection()
            for (i in sectionEntities.indices step 2) {
                keyboardMarkups.add(listOf())
            }
        }

        suspend fun build(): InlineKeyboardMarkup{
            return InlineKeyboardMarkup(keyboardMarkups)
        }
    }
}