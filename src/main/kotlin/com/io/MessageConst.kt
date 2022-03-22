package com.io

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

object KnowWhenStartMessage: Message {
    override val ru: String
        get() = "Сообщите когда решите начать"

    override val en: String
        get() = "Let us know when you decide to start"
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

object StartLearningMessage: Message {
    override val ru: String
        get() = "Начать обучение"
    override val en: String
        get() = "Start learning"

}