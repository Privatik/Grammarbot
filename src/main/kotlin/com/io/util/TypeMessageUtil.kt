package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.cache.entity.Entity
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.resourse.Message
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.ReplyKeyboard
import com.io.util.extends.createMessage

fun TypeMessage.getMessage(): Message{
    return when (this.message.group){
        MessageGroup.START -> Message.StartMessage
        MessageGroup.CHOICE_SECTION -> Message.ChoiceLessonMessage
        MessageGroup.SECTION -> (this as TypeMessage.Section).section.createMessage()
        MessageGroup.LEARN -> (this as TypeMessage.Learn).task.createMessage()
        else -> throw NoSuchMethodError()
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
        else -> null
    }
}