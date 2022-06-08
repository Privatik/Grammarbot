package com.io.util.extends

import com.io.cache.entity.Entity
import com.io.cache.entity.Entity.MessageEntity
import com.io.cache.entity.SectionEntity
import com.io.model.MessageFilter
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT

fun messageTermWithCheckChatId(
    chatId: String,
    block:GetBooleanViaT<MessageEntity>
): Pair<MessageFilter, GetBooleanViaT<Entity>> {
    return MessageFilter.MESSAGE to {
        it as MessageEntity
        it.chatId == chatId && block(it)
    }
}

fun messageTerm(block:GetBooleanViaT<MessageEntity>): Pair<MessageFilter, GetBooleanViaT<Entity>> {
    return MessageFilter.MESSAGE to {
        block(it as MessageEntity)
    }
}

fun sectionTerm(block:GetBooleanViaT<Entity.SectionRuleEntity>): Pair<MessageFilter, GetBooleanViaT<Entity>> {
    return MessageFilter.SECTION to {
        block(it as Entity.SectionRuleEntity)
    }
}

fun taskTerm(block:GetBooleanViaT<Entity.Task>): Pair<MessageFilter, GetBooleanViaT<Entity>> {
    return MessageFilter.TASK to {
        block(it as Entity.Task)
    }
}