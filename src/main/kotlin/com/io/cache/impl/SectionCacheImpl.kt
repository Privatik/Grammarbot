package com.io.cache.impl

import com.io.cache.SectionCache
import com.io.cache.entity.MessageToSection
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.Entity.SectionRuleEntity
import com.io.util.GetBooleanViaT

class SectionCacheImpl(): SectionCache {
    private val messagesWithSection = mutableListOf<MessageToSection>()

    private val sections = listOf<SectionEntity>(
        SectionEntity("past_simple", "Past Simple","Past Simple"),
        SectionEntity("present_simple","Present Simple","Present Simple")
    )

    private val sectionRules = listOf<SectionRuleEntity>(
        SectionRuleEntity("past_simple", "This is rule on english 1", "Это правило на русском 1"),
        SectionRuleEntity("present_simple","This is rule on english 2", "Это правило на русском 2")
    )

    override suspend fun saveMessage(chatId: String, messageId: Int, sectionId: String) {
        messagesWithSection.add(MessageToSection(chatId, messageId, sectionId))
    }

    override suspend fun deleteMessages(messageId: Int, term: GetBooleanViaT<MessageToSection>) {
        messagesWithSection.removeIf { term(it) && messageId == it.messageId }
    }

    override suspend fun getAllSectionInfo(): List<SectionEntity> {
        return sections
    }

    override suspend fun getRules(term: GetBooleanViaT<SectionRuleEntity>): List<SectionRuleEntity> {
        return sectionRules.filter { term(it) }
    }

    override suspend fun getCurrentRules(messageId: Int): SectionRuleEntity {
        val message = messagesWithSection.find { messageId == it.messageId }!!
        return getRules(term = { message.sectionId == it.id }).first()
    }
}