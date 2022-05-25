package com.io.cache.entity

import com.io.model.MessageGroup

data class MessageEntity(
    val id: Long,
    val chatId: String,
    val group: MessageGroup,
)

data class MessageToSection(
    val messageId: Long,
    val sectionId: String
)
