package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.UserState
import com.io.resourse.ChoiceLessonMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.sendMessage

internal suspend fun sendSectionMessage(
    chatId: String,
    sectionId: String,
    language: Language
): List<TelegramMessageHandler.Result>{

    val sectionMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            replyMarkup = InlineKeyBoardMarkupBuilder()
                .addTranslateButton()
                .build()
        ).asSendBehaviour(MessageGroup.SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.PRE_LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsSection(sectionId)
    )

    return listOf(sectionMessage)

}