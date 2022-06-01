package com.io.cache

import com.io.cache.entity.MessageToSection
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.Entity.SectionRuleEntity
import com.io.util.GetBooleanViaT

interface SectionCache {
    suspend fun saveMessage(chatId: String, messageId: Int, sectionId: String)
    suspend fun deleteMessages(messageId: Int, term: GetBooleanViaT<MessageToSection>)

    suspend fun getAllSectionInfo(): List<SectionEntity>
    suspend fun getRules(term: GetBooleanViaT<SectionRuleEntity>): List<SectionRuleEntity>
    suspend fun getCurrentRules(messageId: Int): SectionRuleEntity
}