package com.io.resourse

import com.io.model.Language

interface Message{
    val ru: String
    val en: String
    val callBack: String

    companion object {
        val StartMessage = Const(
            ru = "Этот бот для изучения грамматики",
            en = "This bot for learning grammar",
            callBack = "startMessage"
        )

        val TranslateMessage = Const(
            ru = "Перевести",
            en = "Translate",
            callBack = "tranlate"
        )

        val ChoiceLessonMessage = Const(
            ru = "Выберите урок",
            en = "Choice lesson",
            callBack = "choiceLesson"
        )

        val NextMessage = Const(
            ru = "Далее",
            en = "Next",
            callBack = "nextTask"
        )

        val PutTaskMessage = Const(
            ru = "Вставка",
            en = "Put",
            callBack = "putTask"
        )

        val WriteTaskMessage = Const(
            ru = "Перевод",
            en = "Translate",
            callBack = "writeTask"
        )

        val FinishLessonMessage = Const(
            ru = "Закончить урок",
            en = "Finish lesson",
            callBack = "finishLesson"
        )

        val BackLessonMessage = Const(
            ru = "Назад",
            en = "Back",
            callBack = "back"
        )
    }

    data class Const(
        override val ru: String,
        override val en: String,
        override val callBack: String
    ): Message

    fun get(language: Language):String{
        return when (language){
            Language.RU -> ru
            Language.EN -> en
        }
    }
}