package com.io.service

import com.io.service.entity.SectionEntity
import com.io.service.entity.SectionRuleEntity

interface SectionCache {

    suspend fun saveSection(sectionEntity: SectionEntity): Long

    suspend fun getAllSection(): List<SectionEntity>

    suspend fun saveRule(sectionRuleEntity: SectionRuleEntity)
}