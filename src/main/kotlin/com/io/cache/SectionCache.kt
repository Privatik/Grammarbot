package com.io.cache

import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity

interface SectionCache {
    suspend fun getAllSection(): List<SectionEntity>
    suspend fun getCurrentRules(sectionId: String): SectionRuleEntity
}