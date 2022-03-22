package com.io.service.impl

import com.io.service.SectionCache
import com.io.service.entity.SectionEntity
import com.io.service.entity.SectionRuleEntity

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