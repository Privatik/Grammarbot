package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.createAnswerMessage
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.getAnswerInlineKeyboardMarkup

suspend fun sendFinishMessage(
    userEntity: UserEntity,
    currentMessageId: Int,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>,
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    if (userEntity.currentState != UserState.LEARN){
        val deleteMessage = TelegramMessageHandler.Result.Ordinary(
            chatId = chatId,
            behaviour = deleteMessage(
                chat_id = chatId,
                messageId = currentMessageId
            ).asDeleteBehaviour(MessageGroup.NONE.name),
            finishBehaviorUser = UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
        return listOf(deleteMessage)
    }

    val filterMessage = messageTermWithCheckChatId(chatId){
        it.group == MessageGroup.RIGHT_ANSWER_ON_TASK
                ||  it.group == MessageGroup.ANSWER_ON_TASK
                ||  it.group == MessageGroup.TASK
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
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsTask(task.id, task.state)
    )

    return listOf(
        answerMessage
    )
}