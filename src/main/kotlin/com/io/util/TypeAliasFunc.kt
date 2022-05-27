package com.io.util

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.cache.entity.UserEntity
import com.io.model.MessageGroup

typealias GetMessageEntityViaIntToMessageGroup = suspend (messageId: Long, group: MessageGroup) -> MessageEntity?
typealias GetUserEntity = suspend () -> UserEntity?
typealias GetBooleanViaT<T> = (T) -> Boolean

typealias GetListRViaFuncT<T, R> = suspend (GetBooleanViaT<T>) -> List<R>

typealias GetString = suspend () -> String