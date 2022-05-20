package com.io.util

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.model.MessageGroup

typealias GetMessageEntityViaIntToMessageGroup = suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?
typealias GetUserEntity = suspend () -> UserEntity


typealias GetBooleanViaMessageEntity = suspend (MessageEntity) -> Boolean

typealias GetMessageGroupToIntsViaFuncMessageEntity = suspend (GetBooleanViaMessageEntity) -> Map<MessageGroup, List<Int>>