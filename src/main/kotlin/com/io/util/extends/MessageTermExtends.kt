package com.io.util.extends

import com.io.cache.entity.MessageEntity
import com.io.util.GetBooleanViaT

fun messageTermWithCheckChatId(chatId: String, block:GetBooleanViaT<MessageEntity>): GetBooleanViaT<MessageEntity> {
    return {
        it.chatId == chatId && block(it)
    }
}

fun messageTerm(block:GetBooleanViaT<MessageEntity>): GetBooleanViaT<MessageEntity> {
    return {
        block(it)
    }
}