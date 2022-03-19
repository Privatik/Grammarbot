package com.io.util

interface Message{
    val ru: String
    val en: String

    enum class Language{
        RU,
        EN
    }

    fun get(language: Language):String{
        return when (language){
            Language.RU -> ru
            Language.EN -> en
        }
    }
}

object StartMessage: Message{
    override val ru: String
        get() = "Этот бот для изучения грамматики"

    override val en: String
        get() = "This bot for learning grammar"
}