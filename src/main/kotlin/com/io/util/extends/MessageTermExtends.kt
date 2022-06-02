package com.io.util.extends

import com.io.cache.entity.Entity
import com.io.cache.entity.Entity.MessageEntity
import com.io.cache.entity.SectionEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT

fun messageTermWithCheckChatId(
    chatId: String,
    group: MessageGroup = MessageGroup.START,
    block:GetBooleanViaT<MessageEntity>
): Pair<MessageGroup, GetBooleanViaT<Entity>> {
    return group to {
        it as MessageEntity
        it.chatId == chatId && block(it)
    }
}

fun messageTerm(group: MessageGroup = MessageGroup.START, block:GetBooleanViaT<MessageEntity>): Pair<MessageGroup, GetBooleanViaT<Entity>> {
    return group to {
        block(it as MessageEntity)
    }
}

fun sectionTerm(block:GetBooleanViaT<Entity.SectionRuleEntity>): Pair<MessageGroup, GetBooleanViaT<Entity>> {
    return MessageGroup.SECTION to {
        block(it as Entity.SectionRuleEntity)
    }
}

fun taskTerm(block:GetBooleanViaT<Entity.Task>): Pair<MessageGroup, GetBooleanViaT<Entity>> {
    return MessageGroup.LEARN to {
        block(it as Entity.Task)
    }
}