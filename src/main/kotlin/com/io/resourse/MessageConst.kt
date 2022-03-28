package com.io.resourse

import com.io.model.Language

interface Message{
    val ru: String
    val en: String

    fun get(language: Language):String{
        return when (language){
            Language.RU -> ru
            Language.EN -> en
        }
    }
}

object StartMessage: Message {
    override val ru: String
        get() = "Этот бот для изучения грамматики"

    override val en: String
        get() = "This bot for learning grammar"
}

object TranslateMessage: Message {
    override val ru: String
        get() = "Перевести"

    override val en: String
        get() = "Translate"
}

object ChoiceLessonMessage: Message {
    override val ru: String
        get() = "Выберите урок"
    override val en: String
        get() = "Choice lesson"

}