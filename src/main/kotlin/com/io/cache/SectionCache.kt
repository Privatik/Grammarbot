package com.io.cache

import com.io.cache.entity.SectionEntity
import com.io.cache.entity.SectionRuleEntity

interface SectionCache {

    fun saveSection(sectionEntity: SectionEntity): Long

    fun getAllSection(): List<SectionEntity>

    fun saveRule(sectionRuleEntity: SectionRuleEntity)
}