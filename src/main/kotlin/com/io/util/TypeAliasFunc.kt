package com.io.util

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.model.MessageGroup

typealias GetMessageEntityViaIntToMessageGroup = suspend (messageId: Int, group: MessageGroup) -> Entity.MessageEntity?
typealias GetUserEntity = suspend () -> UserEntity?
typealias GetBooleanViaT<T> = (T) -> Boolean

typealias GetListRViaFuncT<T, R> = suspend (Pair<MessageGroup,GetBooleanViaT<T>>) -> List<R>

typealias GetString = suspend () -> String