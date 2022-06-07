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
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.extends.taskTerm
import com.io.util.getMessage
import kotlin.random.Random

suspend fun sendTaskMessage(
    userEntity: UserEntity,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>,
    state: LessonState
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filterMessage = messageTermWithCheckChatId(chatId, MessageGroup.START){
        it.group == MessageGroup.SECTION
    }

    val message = messageIds(filterMessage).first() as TypeMessage.Section

    val filterTask = taskTerm {
        it.state == state && it.sectionId == message.section.id
    }

    val task = messageIds(filterTask).let {
        it[Random.nextInt(it.size)] as TypeMessage.Learn
    }

    val sendTask = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = task.getMessage().get(language)
        ).asSendBehaviour(MessageGroup.LEARN.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsTask(task.task.id, state)
    )

    return listOf(sendTask)
}