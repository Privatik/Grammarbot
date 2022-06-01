package com.io.resourse

import com.io.cache.entity.Entity
import com.io.model.MessageGroup

val emptyMessageEntity = Entity.MessageEntity(
    id = -1,
    chatId = "",
    group = MessageGroup.NONE
)
