package com.io.cache.entity

import com.io.model.MessageGroup

data class MessageEntity(
    val id: Int,
    val chatId: String,
    val group: String
)
