package com.io.util.extends

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.TypeMessage
import com.io.util.GetListRViaFuncT
import com.io.util.GetMessageEntityViaIntToMessageGroup
import com.io.util.GetUserEntity


suspend fun TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>.getUserToMessageIds(
    id: String
): Pair<UserEntity, GetListRViaFuncT<Entity, TypeMessage>> {
    val currentUser = processingUser(id, UserInteractor.BehaviorForUser.GetOrCreate).invoke()
    val messageIds = getMessages(id)
    return currentUser!! to messageIds
}