package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.SectionCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.MessageToSection
import com.io.cache.entity.SectionRuleEntity
import com.io.model.TypeMessage
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT
import com.io.util.GetString

class MessageCacheImpl(
    private val sectionCache: SectionCache
): MessageCache {
    private val messages = mutableListOf<MessageEntity>()
    private val messagesWithSection = mutableListOf<MessageToSection>()

    private fun print(){
        println("Message $messages")
    }


    override suspend fun saveMessageId(chatId: String, messageId: Long, messageGroup: MessageGroup): MessageEntity {
        val entity = MessageEntity(
            id = messageId,
            chatId = chatId,
            group = messageGroup
        )
        messages.add(
            entity
        )
        print()
        return entity
    }

    override suspend fun deleteMessageId(chatId: String, messageId: Long): MessageEntity {
        messagesWithSection.find { it.messageId == messageId}?.run {
            messagesWithSection.removeAt(messagesWithSection.indexOf(this))
        }
        return messages.removeAt(messages.indexOfFirst { it.id == messageId && it.chatId == chatId })
    }

    override suspend fun getMessages(chatId: String, term: GetBooleanViaT<MessageEntity>): List<TypeMessage> {
        return messages.filter{ term(it) && it.chatId == chatId }.map { it.transform() }
    }

    override suspend fun getSettingRuleEntity(get: GetString): SectionRuleEntity {
        return sectionCache.getCurrentRules(get())
    }

    private suspend fun MessageEntity.transform(): TypeMessage{
        return messagesWithSection.find { it.messageId == id }?.let {
            TypeMessage.Section(this, sectionCache.getCurrentRules(it.sectionId))
        } ?: let { TypeMessage.Simple(this) }
    }

}