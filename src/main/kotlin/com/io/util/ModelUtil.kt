package com.io.util

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.Message
import com.io.resourse.StartMessage

suspend fun TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>.getUserToMessageIds(
    id: String
): Pair<UserEntity, GetMessageGroupToIntsViaFuncMessageEntity> {
    val currentUser = processingUser(id, UserInteractor.BehaviorForUser.GetOrCreate).invoke()
    val messageIds = getMessagesAsMapByMessageGroup(id)
    return currentUser!! to messageIds
}

fun MessageEntity.getMessage(): Message{
    return when (this.group){
        MessageGroup.START -> StartMessage
        MessageGroup.CHOICE_SECTION -> ChoiceLessonMessage
        MessageGroup.SECTION ->
        else -> throw NoSuchMethodError()
    }
}