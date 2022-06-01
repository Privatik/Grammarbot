package com.io.util.extends

import com.io.cache.entity.Entity
import com.io.cache.entity.Entity.MessageEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT

fun messageTermWithCheckChatId(
    chatId: String,
    group: MessageGroup = MessageGroup.START,
    block:GetBooleanViaT<MessageEntity>
): GetBooleanViaT<MessageEntity> {
    return {
        it.chatId == chatId && block(it)
    }
}

fun messageTerm(group: MessageGroup = MessageGroup.START, block:GetBooleanViaT<MessageEntity>): GetBooleanViaT<MessageEntity> {
    return {
        block(it)
    }
}

fun sectionTerm(block:GetBooleanViaT<Entity.SectionRuleEntity>): GetBooleanViaT<Entity.SectionRuleEntity> {
    return {
        block(it)
    }
}