package com.io.cache.impl

import com.io.cache.SectionCache
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity

class SectionCacheImpl: SectionCache {
    val sections = listOf<SectionEntity>(
        SectionEntity(1, "Past Simple"),
        SectionEntity(2,"Present Simple")
    )

    val sectionRules = listOf<SectionRuleEntity>(
        SectionRuleEntity(1, "This is rule on english 1", "Это правило на русском 1"),
        SectionRuleEntity(2,"This is rule on english 2", "Это правило на русском 2")
    )

    override suspend fun getAllSection(): List<SectionEntity> {
        return sections
    }

    override suspend fun getCurrentRules(sectionId: Long): SectionRuleEntity {
        return sectionRules.find { sectionId == it.id }!!
    }
}