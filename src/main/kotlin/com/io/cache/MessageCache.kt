package com.io.cache

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.model.TypeMessage
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT
import com.io.util.GetString


interface MessageCache {
    suspend fun saveMessageId(chatId: String, messageId: Long, messageGroup: MessageGroup): MessageEntity

    suspend fun deleteMessageId(chatId: String, messageId: Long): MessageEntity

    suspend fun getMessages(chatId: String, term: GetBooleanViaT<MessageEntity>): List<TypeMessage>

    suspend fun getSettingRuleEntity(get: GetString): SectionRuleEntity
}