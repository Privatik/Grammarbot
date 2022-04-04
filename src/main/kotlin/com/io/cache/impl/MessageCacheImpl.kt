package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup
import com.io.model.asMessageGroup

class MessageCacheImpl: MessageCache {
    private val messages = mutableListOf<MessageEntity>()

    private fun print(){
        println("Message $messages")
    }

    override suspend fun saveMessageIds(chatId: String, message: Pair<MessageGroup, Int>): Boolean {
        messages.add(
            MessageEntity(
                id = message.second,
                chatId = chatId,
                group = message.first.name
            )
        )
        print()
        return true
    }


    override suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): Map<MessageGroup, List<Int>> {
        return messages.filter{ term(it) && it.chatId == chatId }.groupByTo(hashMapOf(), {it.group.asMessageGroup() }, {it.id})

    }

    override suspend fun deleteMessageId(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.removeIf { it.chatId == chatId }
        }
        print()
    }
}