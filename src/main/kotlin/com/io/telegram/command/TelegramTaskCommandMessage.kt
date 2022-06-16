package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.LessonState
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.editMessageText
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.createAnswerMessage
import com.io.util.extends.createMessage
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.extends.taskTerm
import com.io.util.getTaskReplyKeyboardMarkup
import kotlin.random.Random

suspend fun sendTaskMessage(
    userEntity: UserEntity,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>,
    state: LessonState? = null
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filterMessage = messageTermWithCheckChatId(chatId){ it.group == MessageGroup.SECTION }
    val filterHaveTask = messageTermWithCheckChatId(chatId){ it.group == MessageGroup.RIGHT_ANSWER_ON_TASK }
    val sectionMessage = messageIds(filterMessage).first() as TypeMessage.Section

    val oldTasks = messageIds(filterHaveTask)
    val isFirstTask = oldTasks.isEmpty()

    val correctState: LessonState = state ?: (oldTasks.first() as TypeMessage.Learn).task.state
    val filterTask = taskTerm { it.state == correctState && it.sectionId == sectionMessage.section.id }
    val taskMessage = messageIds(filterTask).let { it.random() as TypeMessage.Learn }
    val result = mutableListOf<TelegramMessageHandler.Result>()

    if (isFirstTask){
        val updateSectionMessage = TelegramMessageHandler.Result.Ordinary(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                messageId = sectionMessage.message.id,
                text = sectionMessage.section.createMessage().get(language),
                replyMarkup = null
            ).asSendBehaviour(MessageGroup.SECTION.name),
            finishBehaviorUser = UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
        result.add(updateSectionMessage)
    } else {
        val lastAnswer = oldTasks.maxByOrNull { it.message.id } as TypeMessage.Learn
        val updateLastRightAnswerMessage = TelegramMessageHandler.Result.Ordinary(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                messageId = lastAnswer.message.id,
                text = lastAnswer.task.createAnswerMessage().get(language),
                replyMarkup = null
            ).asSendBehaviour(MessageGroup.SECTION.name),
            finishBehaviorUser = UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
        result.add(updateLastRightAnswerMessage)
    }

    val sendTask = TelegramMessageHandler.Result.Ordinary(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = taskMessage.task.createMessage().get(language),
            replyMarkup = getTaskReplyKeyboardMarkup(taskMessage.task, language)
        ).asSendBehaviour(MessageGroup.TASK.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsTask(taskMessage.task.id, correctState)
    )

    result.add(sendTask)

    return result
}