package com.io.util

import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.resourse.Message
import com.io.telegram.InlineKeyboardMarkup
import com.io.util.extends.createAnswerMessage
import com.io.util.extends.createMessage

fun TypeMessage.getMessage(): Message{
    return when (this.message.group){
        MessageGroup.START -> Message.StartMessage
        MessageGroup.CHOICE_SECTION -> Message.ChoiceLessonMessage
        MessageGroup.SECTION -> (this as TypeMessage.Section).section.createMessage()
        MessageGroup.TASK -> (this as TypeMessage.Learn).task.createMessage()
        MessageGroup.ANSWER_ON_TASK -> (this as TypeMessage.Learn).task.createAnswerMessage()
        else -> throw NoSuchMethodError("Dont found \"getMessage\" group")
    }
}
fun TypeMessage.getReplyKeyboard(state: UserState, language: Language): InlineKeyboardMarkup?{

    return when (this.message.group){
        MessageGroup.CHOICE_SECTION -> {
            if (state == UserState.RELAX){
                getSectionMenuInlineKeyboardMarkup(language,(this as TypeMessage.SectionMenu).sections)
            } else null
        }
        MessageGroup.SECTION -> {
            if (state != UserState.LEARN){
                getSectionInlineKeyboardMarkup(language)
            } else null
        }
        MessageGroup.ANSWER_ON_TASK -> {
            getAnswerInlineKeyboardMarkup(language)
        }
        else -> null
    }
}