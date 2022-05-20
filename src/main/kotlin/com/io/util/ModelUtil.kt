package com.io.util

import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor

suspend fun TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>.getUserToMessageIds(
    id: String
): Pair<UserEntity, GetMessageGroupToIntsViaFuncMessageEntity> {
    val currentUser = processingUser(id, UserInteractor.BehaviorForUser.GetOrCreate).invoke()
    val messageIds = getMessagesAsMapByMessageGroup(id)
    return currentUser!! to messageIds
}