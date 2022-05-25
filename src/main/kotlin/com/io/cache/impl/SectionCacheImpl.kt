package com.io.cache.impl

import com.io.cache.SectionCache
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity

class SectionCacheImpl: SectionCache {
    val sections = listOf<SectionEntity>(
        SectionEntity("past_simple", "Past Simple","Past Simple"),
        SectionEntity("present simple","Present Simple","Present Simple")
    )

    val sectionRules = listOf<SectionRuleEntity>(
        SectionRuleEntity("past_simple", "This is rule on english 1", "Это правило на русском 1",false, false),
        SectionRuleEntity("present simple","This is rule on english 2", "Это правило на русском 2",false, false)
    )

    override suspend fun getAllSection(): List<SectionEntity> {
        return sections
    }

    override suspend fun getCurrentRules(sectionId: String): SectionRuleEntity {
        return sectionRules.find { sectionId == it.id }!!
    }
}