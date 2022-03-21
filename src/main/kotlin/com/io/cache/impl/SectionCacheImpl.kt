package com.io.cache.impl

import com.io.cache.SectionCache
import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity

class SectionCacheImpl: SectionCache {

    override suspend fun saveSection(sectionEntity: SectionEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSection(): List<SectionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRule(sectionRuleEntity: SectionRuleEntity) {
        TODO("Not yet implemented")
    }
}