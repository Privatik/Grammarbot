package com.io.cache

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.model.MessageGroup
import com.io.util.GetString


interface MessageCache {
    suspend fun saveMessageId(chatId: String, message: Pair<Int, MessageGroup>): MessageEntity

    suspend fun deleteMessageId(chatId: String, messageId: Int): MessageEntity

    suspend fun getMessageIds(chatId: String, term: suspend (MessageEntity) -> Boolean): List<MessageEntity>

    suspend fun getSettingRuleEntity(get: GetString): SectionRuleEntity
}