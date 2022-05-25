package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupMachine
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.telegram.*
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetBooleanViaT
import com.io.util.GetListRViaFuncT

internal suspend inline fun sendStartMessage(
    chatId: String,
    messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: GetBooleanViaT<MessageEntity> = {
        it.group == MessageGroup.START || it.group == MessageGroup.CHOICE_SECTION
    }

    val hasMessage = messageIds(filter).isNotEmpty()

    if (hasMessage){
        return emptyList()
    }

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour =  sendMessage(
            chat_id = chatId,
            text = StartMessage.get(language),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            replyMarkup = InlineKeyBoardMarkupMachine.Builder()
                .addTranslateButton()
                .addSectionButton()
                .build()
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}