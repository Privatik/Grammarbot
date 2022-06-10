package com.io.util

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.model.MessageFilter
import com.io.model.MessageGroup
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.TelegramRequest

typealias GetMessageEntityViaIntToMessageGroup = suspend (messageId: Int, group: MessageGroup) -> Entity.MessageEntity?
typealias GetUserEntity = suspend () -> UserEntity?
typealias GetBooleanViaT<T> = (T) -> Boolean

typealias GetListRViaFuncT<T, R> = suspend (Pair<MessageFilter, GetBooleanViaT<T>>) -> List<R>

typealias GetString = suspend () -> String

typealias CreateTelegramMessageHandlerResult = (messageId: Int) -> TelegramMessageHandler.Result