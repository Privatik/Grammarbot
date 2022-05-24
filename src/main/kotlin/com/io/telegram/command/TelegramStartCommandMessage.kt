package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupMachine
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.editMessageText
import com.io.telegram.sendMessage
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetMessageGroupToIntsViaFuncMessageEntity

internal suspend fun sendStartMessage(
    chatId: String,
    messageIds: GetMessageGroupToIntsViaFuncMessageEntity,
    language: Language
): List<TelegramMessageHandler.Result>{
    val deleteMessages = mutableListOf<TelegramMessageHandler.Result>()

    val filter: GetBooleanViaMessageEntity = {
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

internal suspend fun editStartMessage(
    chatId: String,
    messageIds: GetMessageGroupToIntsViaFuncMessageEntity,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: suspend (com.io.cache.entity.MessageEntity) -> Boolean = {
        it.group == MessageGroup.START || it.group == MessageGroup.CHOICE_SECTION
    }
    val newMessageIds = messageIds(filter)

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = StartMessage.get(language),
            messageId = newMessageIds[MessageGroup.START]!!.first(),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(language = language),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            messageId = newMessageIds[MessageGroup.CHOICE_SECTION]!!.first(),
            replyMarkup = InlineKeyBoardMarkupMachine.Builder()
                .addTranslateButton()
                .addSectionButton()
                .build()
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}