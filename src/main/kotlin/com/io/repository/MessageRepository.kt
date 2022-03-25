package com.io.repository

import com.io.model.MessageGroup
import com.io.service.entity.MessageEntity

interface MessageRepository {
    suspend fun saveMessageIds(chatId: String, messages: List<Int>)

    suspend fun getMessageIds(chatId: String, term: (MessageGroup) -> Boolean): List<Int>

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)
}