package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.createAnswerMessage
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.getAnswerInlineKeyboardMarkup

suspend fun sendAnswerTaskMessage(
    userEntity: UserEntity,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>,
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filterMessage = messageTermWithCheckChatId(chatId){
        it.group == MessageGroup.TASK
    }

    val learnMessage = messageIds(filterMessage).maxByOrNull { it.message.time }!! as TypeMessage.Learn
    val task = learnMessage.task

    val answerMessage = TelegramMessageHandler.Result.Ordinary(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = task.createAnswerMessage().get(language),
            replyMarkup = getAnswerInlineKeyboardMarkup(language)
        ).asSendBehaviour(MessageGroup.RIGHT_ANSWER_ON_TASK.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.PAUSE_LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsTask(task.id, task.state)
    )

    return listOf(
        answerMessage
    )
}